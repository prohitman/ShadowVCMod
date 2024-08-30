package com.prohitman.shadowvcmod.vc;

import com.prohitman.shadowvcmod.config.ServerConfig;
import com.prohitman.shadowvcmod.entity.VCZombieEntity;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.*;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@ForgeVoicechatPlugin
public class ShadowVCPlugin implements VoicechatPlugin {
    public static final Map<UUID, OpusDecoder> playerDecoders = new HashMap<>();

    @Override
    public String getPluginId() {
        return "vcshadowmod";
    }

    @Override
    public void initialize(VoicechatApi api) {
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(PlayerConnectedEvent.class, this::onPlayerConnected);
        registration.registerEvent(PlayerDisconnectedEvent.class, this::onPlayerDisconnected);
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicPacket);
    }

    private void onMicPacket(MicrophonePacketEvent event) {
        VoicechatConnection senderConnection = event.getSenderConnection();
        if (senderConnection == null) {
            return;
        }

        if (event.getPacket().getOpusEncodedData().length <= 0) {
            // Don't trigger any events when stopping to talk
            return;
        }

        if (!ServerConfig.groupInteraction.get()) {
            if (senderConnection.isInGroup()) {
                return;
            }
        }

        if (!ServerConfig.whisperInteraction.get()) {
            if (event.getPacket().isWhispering()) {
                return;
            }
        }

        if (!(senderConnection.getPlayer().getPlayer() instanceof ServerPlayer player)) {
            return;
        }

        if (!ServerConfig.sneakInteraction.get()) {
            if (player.isCrouching()) {
                return;
            }
        }

        OpusDecoder decoder = playerDecoders.get(player.getUUID());

        if (decoder == null) {
            return;
        }

        decoder.resetState();

        short[] decoded = decoder.decode(event.getPacket().getOpusEncodedData());

        double audioLevel = AudioUtils.calculateAudioLevel(decoded);

        double minDecibelThreshold = ServerConfig.minDecibelThreshold.get().doubleValue();

        int detectionRange = ServerConfig.minDetectionRange.get();

        if(audioLevel < minDecibelThreshold){
            //Don't trigger anything when the audioLevel is below the threshold
            return;
        } else {
            detectionRange += convertDecibelToRange(audioLevel);
            //System.out.println("Sound heard, calculated detection range: " + detectionRange);
        }

        int finalDetectionRange = detectionRange;
        player.getLevel().getServer().execute(() -> {
            List<VCZombieEntity> vczombies;

            vczombies = player.level.getEntitiesOfClass(VCZombieEntity.class, player.getBoundingBox().inflate(finalDetectionRange));
            if(!vczombies.isEmpty()){
                for(VCZombieEntity zombie : vczombies){
                    zombie.setTarget(player);
                }
            }
        });
    }

    public int convertDecibelToRange(double decibel) {
        int minDecibelThreshold = ServerConfig.minDecibelThreshold.get();
        if (decibel > 0 || decibel < -127) {
            throw new IllegalArgumentException("Decibel value must be between -127 and 0.");
        }

        if (decibel < minDecibelThreshold) {
            return 0;
        }

        float normalizedValue = (float)(decibel - minDecibelThreshold) / (-minDecibelThreshold);

        int detectionRange = Math.round(normalizedValue * 32);

        return Math.min(detectionRange, 32);
    }

    private void onPlayerConnected(PlayerConnectedEvent event) {
        VoicechatServerApi api = event.getVoicechat();
        VoicechatConnection connection = event.getConnection();
        de.maxhenkel.voicechat.api.ServerPlayer player = connection.getPlayer();
        UUID uuid = player.getUuid();

        OpusDecoder decoder = playerDecoders.computeIfAbsent(uuid, k -> api.createDecoder());
        playerDecoders.putIfAbsent(uuid, decoder);
    }

    private void onPlayerDisconnected(PlayerDisconnectedEvent event) {
        UUID uuid = event.getPlayerUuid();

        OpusDecoder decoder = playerDecoders.getOrDefault(uuid, null);
        if (decoder != null) {
            decoder.close();
        }

        playerDecoders.remove(uuid);
    }
}

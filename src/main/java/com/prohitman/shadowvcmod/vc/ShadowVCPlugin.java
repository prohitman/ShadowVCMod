package com.prohitman.shadowvcmod.vc;

import com.prohitman.shadowvcmod.config.ServerConfig;
import com.prohitman.shadowvcmod.entity.VCZombieEntity;
import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@ForgeVoicechatPlugin
public class ShadowVCPlugin implements VoicechatPlugin {
    public static VoicechatApi voicechatApi;
    private static ConcurrentHashMap<UUID, Long> cooldowns;

    @Nullable
    public static VoicechatServerApi voicechatServerApi;

    @Nullable
    private OpusDecoder decoder;

    @Override
    public String getPluginId() {
        return "vcshadowmod";
    }

    @Override
    public void initialize(VoicechatApi api) {
        voicechatApi = api;
        cooldowns = new ConcurrentHashMap<>();
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicPacket);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        voicechatServerApi = event.getVoicechat();
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
            //VoicechatInteraction.LOGGER.warn("Received microphone packets from non-player");
            return;
        }

        if (!ServerConfig.sneakInteraction.get()) {
            if (player.isCrouching()) {
                return;
            }
        }

        if (decoder == null) {
            decoder = event.getVoicechat().createDecoder();
        }

        decoder.resetState();
        short[] decoded = decoder.decode(event.getPacket().getOpusEncodedData());

        double audioLevel = AudioUtils.calculateAudioLevel(decoded);

        //double minSuspicionLevel = ServerConfig.minSuspicionActivationThreshold.get().doubleValue();
        double minDecibelThreshold = ServerConfig.minDecibelThreshold.get().doubleValue();
        int detectionRange = ServerConfig.minDetectionRange.get();

        if(audioLevel < minDecibelThreshold){
            return;
        } else {
            detectionRange += convertDecibelToRange(audioLevel);
            System.out.println("Sound heard, detection range: " + detectionRange);
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
            /*boolean isSuspicious = false;
            boolean shouldTarget = false;

            if (audioLevel < minSuspicionLevel) {
                System.out.println("Voice too low");
                return;
            }
            else if(minSuspicionLevel < audioLevel && audioLevel < minTargetLevel){
                System.out.println("Voice reached suspicion level");
                isSuspicious = true;
            } else if(minTargetLevel < audioLevel){
                System.out.println("Voice too loud");
                shouldTarget = true;
            }

            List<VCZombieEntity> vczombies;
            vczombies = player.level.getEntitiesOfClass(VCZombieEntity.class, player.getBoundingBox().inflate(ServerConfig.minDetectionRange.get()));

            if(!vczombies.isEmpty()){
                for(VCZombieEntity entity : vczombies){
                    if(isSuspicious){
                        entity.setSuspicionPos(player.getOnPos().above());//Add go to sus pos goal
                    } else if(shouldTarget){
                        entity.setTarget(player);//Add Code to remember targets, and check for isAlive
                    }
                }
            }*/
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
}

package com.infamous.dungeons_libraries.network;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.integration.curios.client.message.CuriosArtifactStartMessage;
import com.infamous.dungeons_libraries.integration.curios.client.message.CuriosArtifactStopMessage;
import com.infamous.dungeons_libraries.network.gearconfig.*;
import com.infamous.dungeons_libraries.network.materials.ArmorMaterialSyncPacket;
import com.infamous.dungeons_libraries.network.materials.WeaponMaterialSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
                    new ResourceLocation(DungeonsLibraries.MODID, "network"))
            .clientAcceptedVersions("1"::equals)
            .serverAcceptedVersions("1"::equals)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    protected static int PACKET_COUNTER = 0;

    public NetworkHandler() {
    }

    public static void init() {
        INSTANCE.messageBuilder(UpdateSoulsMessage.class, incrementAndGetPacketCounter())
                .encoder(UpdateSoulsMessage::encode).decoder(UpdateSoulsMessage::decode)
                .consumerMainThread(UpdateSoulsMessage.UpdateSoulsHandler::handle)
                .add();
        INSTANCE.messageBuilder(ArmorGearConfigSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(ArmorGearConfigSyncPacket::encode).decoder(ArmorGearConfigSyncPacket::decode)
                .consumerMainThread(ArmorGearConfigSyncPacket::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(MeleeGearConfigSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(MeleeGearConfigSyncPacket::encode).decoder(MeleeGearConfigSyncPacket::decode)
                .consumerMainThread(MeleeGearConfigSyncPacket::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(BowGearConfigSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(BowGearConfigSyncPacket::encode).decoder(BowGearConfigSyncPacket::decode)
                .consumerMainThread(BowGearConfigSyncPacket::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(CrossbowGearConfigSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(CrossbowGearConfigSyncPacket::encode).decoder(CrossbowGearConfigSyncPacket::decode)
                .consumerMainThread(CrossbowGearConfigSyncPacket::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(ArmorMaterialSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(ArmorMaterialSyncPacket::encode).decoder(ArmorMaterialSyncPacket::decode)
                .consumerMainThread(ArmorMaterialSyncPacket::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(WeaponMaterialSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(WeaponMaterialSyncPacket::encode).decoder(WeaponMaterialSyncPacket::decode)
                .consumerMainThread(WeaponMaterialSyncPacket::onPacketReceived)
                .add();
        INSTANCE.messageBuilder(CuriosArtifactStartMessage.class, incrementAndGetPacketCounter())
                .encoder(CuriosArtifactStartMessage::encode).decoder(CuriosArtifactStartMessage::decode)
                .consumerMainThread(CuriosArtifactStartMessage.CuriosArtifactHandler::handle)
                .add();
        INSTANCE.messageBuilder(CuriosArtifactStopMessage.class, incrementAndGetPacketCounter())
                .encoder(CuriosArtifactStopMessage::encode).decoder(CuriosArtifactStopMessage::decode)
                .consumerMainThread(CuriosArtifactStopMessage::handle)
                .add();
        INSTANCE.messageBuilder(EliteMobMessage.class, incrementAndGetPacketCounter())
                .encoder(EliteMobMessage::encode).decoder(EliteMobMessage::decode)
                .consumerMainThread(EliteMobMessage::handle)
                .add();
        INSTANCE.messageBuilder(BreakItemMessage.class, incrementAndGetPacketCounter())
                .encoder(BreakItemMessage::encode).decoder(BreakItemMessage::decode)
                .consumerMainThread(BreakItemMessage.BreakItemHandler::handle)
                .add();
        INSTANCE.messageBuilder(SwitchHandMessage.class, incrementAndGetPacketCounter())
                .encoder(SwitchHandMessage::encode).decoder(SwitchHandMessage::decode)
                .consumerMainThread(SwitchHandMessage.SwitchHandHandler::handle)
                .add();
        INSTANCE.messageBuilder(ArtifactGearConfigSyncPacket.class, incrementAndGetPacketCounter())
                .encoder(ArtifactGearConfigSyncPacket::encode).decoder(ArtifactGearConfigSyncPacket::decode)
                .consumerMainThread(ArtifactGearConfigSyncPacket::onPacketReceived)
                .add();
    }

    public static int incrementAndGetPacketCounter() {
        return PACKET_COUNTER++;
    }
}

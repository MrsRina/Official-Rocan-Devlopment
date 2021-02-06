package me.rina.rocan.client.event.network;

import me.rina.rocan.api.event.Event;
import me.rina.rocan.api.event.impl.EventStage;
import net.minecraft.network.Packet;

/**
 * @author SrRina
 * @since 03/02/2021 at 22:47
 **/
public class ReceiveEventPacket extends Event {
    private Packet<?> packet;

    public ReceiveEventPacket(Packet<?> packet) {
        super(EventStage.PRE);

        this.packet = packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

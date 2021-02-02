package me.rina.rocan.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.rina.rocan.Rocan;
import me.rina.rocan.client.event.network.EventPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author SrRina
 * @since 16/11/20 at 10:05pm
 */
@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        EventPacket.Send event = new EventPacket.Send(packet);

        Rocan.getEventManager().dispatch(event);

        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        EventPacket.Receive event = new EventPacket.Receive(packet);

        Rocan.getEventManager().dispatch(event);

        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }
}

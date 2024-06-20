package fun.blonks.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import fun.blonks.Taps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public class VillagerDeathMixin {

	private static final Logger log = LoggerFactory.getLogger(VillagerDeathMixin.class);

	@Inject(at = @At("TAIL"), method = "onDeath")
	private void onDeath(DamageSource source, CallbackInfo ci) {
		Entity attacker = source.getAttacker();
		// Client-side, do nothing
		if (attacker == null) return;

		VillagerEntity killed = (VillagerEntity) source.getSource();
//		TODO: track if traded
//		if (killed.)

		var world = attacker.getEntityWorld();
		var pos = source.getPosition();
		if (!world.isClient && pos != null) {
			world.playSound(
				null,
				pos.x,
				pos.y,
				pos.z,
				Taps.TAPS_EVENT,
				SoundCategory.AMBIENT
			);
		}

		MinecraftServer server = world.getServer();
		if (server != null) {
			Text deathMessage = source.getDeathMessage(killed);
			server.getPlayerManager().broadcast(deathMessage, true);
		}
	}
}
package fun.blonks;

import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Taps implements ModInitializer {
	public static final Identifier TAPS_SOUND_ID = Identifier.of("taps:taps");
	public static SoundEvent TAPS_EVENT = SoundEvent.of(TAPS_SOUND_ID);

	@Override
	public void onInitialize() {
		Registry.register(Registries.SOUND_EVENT, TAPS_SOUND_ID, TAPS_EVENT);
	}
}
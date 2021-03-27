package com.pedrol129.midi;

import java.util.Random;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class InfiniteMusicGenerator {

	@SuppressWarnings({ "squid:S2189" })
	public static void main(String[] args) throws InterruptedException {
		try {
			Synthesizer midiSynth = MidiSystem.getSynthesizer();
			midiSynth.open();

			Instrument[] instr = midiSynth.getDefaultSoundbank().getInstruments();
			MidiChannel[] mChannels = midiSynth.getChannels();

			midiSynth.loadInstrument(instr[0]);

			int[] mixolydian = { 60, 62, 64, 65, 67, 69, 70, 72 };

			int tick = 1;
			int milisPerNote = 200;
			int bar = 8;
			int remainNoteDuration = 0;

			while (true) {
				int index = new Random().nextInt(mixolydian.length - 1);
				int note = mixolydian[index];

				if (tick == 1) {
					int chord1 = note - 24;
					int chord2 = mixolydian[(index + 2) % 8] - 24;
					int chord3 = mixolydian[(index + 4) % 8] - 24;
					int chord4 = mixolydian[(index + 6) % 8] - 24;

					int chordDuration = 200 * 8;
					mChannels[0].noteOn(chord1, chordDuration);
					mChannels[0].noteOn(chord2, chordDuration);
					mChannels[0].noteOn(chord4, chordDuration);
					mChannels[0].noteOn(chord3, chordDuration);

				} else {
					if (tick == bar) {
						tick = 0;
					}

					if (remainNoteDuration == 0) {

						int maxTicks = bar / 2;
						int pendingTicks = bar - tick;

						int maxDuration = maxTicks < pendingTicks ? maxTicks : pendingTicks;
						int minDuration = 1;

						int middle = maxDuration - minDuration;

						if (middle < 1) {
							remainNoteDuration = 1;
						} else {
							remainNoteDuration = (new Random().nextInt(maxDuration - minDuration)) + minDuration;
						}

						mChannels[0].noteOn(note, milisPerNote * remainNoteDuration);

					}
					remainNoteDuration--;

				}

				tick++;

				Thread.sleep(milisPerNote);
			}

		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}

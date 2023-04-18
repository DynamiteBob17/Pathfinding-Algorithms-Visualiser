package hr.mlinx.util;

import javax.sound.midi.*;

public class SoundPlayer {
    private static final int NOTE_MAX = 127;
    private static final int VOLUME = 30;

    private MidiChannel[] mc;
    private int volume;
    private int maxHCost;

    public SoundPlayer() {
        try {
            Synthesizer midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();
            Instrument[] instruments = midiSynth.getAvailableInstruments();
            mc = midiSynth.getChannels();

            // try to set the midi sound to 808 tom preferably,
            // if 808 tom is not possible try to set it to music box,
            // if neither are possible set it to midi at index 0
            String name;
            int midiIdx = 0;
            for (int i = 0; i < instruments.length; ++i) {
                name = instruments[i].getName();

                if (name.toLowerCase().contains("808 tom")
                        || midiIdx == 0 && name.toLowerCase().contains("music box"))
                    midiIdx = i;
            }
            mc[0].programChange(instruments[midiIdx].getPatch().getProgram());
            volume = VOLUME;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public boolean toggleSound() {
        boolean soundOn = volume > 0;

        volume = soundOn ? 0 : VOLUME;

        return !soundOn;
    }

    public void play(int hCost) {
        if (mc != null) {
            int noteNumber = NOTE_MAX - (int) Math.round(Util.map(hCost, 0, maxHCost, 0, NOTE_MAX));
            mc[0].noteOn(noteNumber, volume);
        }
    }

    public void notesOff() {
        if (mc != null) {
            for (int i = 0; i <= NOTE_MAX; ++i) {
                mc[0].noteOff(i);
            }
        }
    }

    public void setMaxHCost(int maxHCost) {
        this.maxHCost = maxHCost;
    }

}

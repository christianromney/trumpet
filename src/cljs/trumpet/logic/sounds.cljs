(ns trumpet.logic.sounds
  "This namespace contains routines that enable the playback of sound using the
  HTML5 Audio API via Tonejs."
  (:require [tonejs]))

(defonce wahwah
  (.toMaster (js/Tone.AutoWah. 58.27 6 -30)))

(defonce synthesizer
  (.toMaster (js/Tone.AMSynth.)))

(defonce output
  (-> synthesizer
      (.connect wahwah)))

(def duration
  "Map of semantic duration names to values
  understood by the synthesizer."
  {:whole-note   "1n"
   :half-note    "2n"
   :quarter-note "4n"
   :eigth-note   "8n"})

(defn play
  "Plays a note in a given octave for the specified duration."
  ([octave-note]
   (play octave-note (duration :quarter-note)))

  ([octave-note time]
   (.triggerAttackRelease output octave-note time)))

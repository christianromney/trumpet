(ns trumpet.sound
  (:require [tonejs]
            [cljs.pprint :refer [pprint]]))

(def config
  (clj->js
   {:oscillator {:type "pwm" :modulationFrequency 0.5}
    :envelope {:attack 2.0
               :decay 0.025
               :sustain 0.25
               :release 0.9}}))

(defonce synthesizer
  (.toMaster (js/Tone.Synth. config)))

(def duration
  "Map of semantic duration names to values
  understood by the synthesizer."
  {:whole-note "1n"
   :half-note "2n"
   :quarter-note "4n"
   :eigth-note "8n"})

(defn play
  "Plays a note in a given octave for the specified duration."
  ([octave-note]
   (play octave-note (duration :quarter-note)))

  ([octave-note time]
   (js/console.log "play: " (clj->js [octave-note time]))
   (.triggerAttackRelease synthesizer octave-note time)))

(defn octave
  "Converts a keyword note and integer octave into
  a string that is understood by the synthesizer.
  (octave :A 4) => 'A4'"
  [note n]
  (str (name note) n))

(ns trumpet.sound
  (:require [tonejs]))

(def config
  (clj->js
   {:oscillator {:type "pwm" :modulationFrequency 0.5}
    :envelope {:attack 2.0
               :decay 0.025
               :sustain 0.25
               :release 0.9}}))

(def synthesizer (.toMaster (js/Tone.Synth. config)))

(def duration
  {:whole-note "1n"
   :half-note "2n"
   :quarter-note "4n"
   :eigth-note "8n"})

(defn play
  ([note]
   (play note (duration :quarter-note)))

  ([note time]
   (.triggerAttackRelease synthesizer note time)))

(defn octave
  [note n]
  (str (name note) n))

(ns trumpet.app
  (:require [reagent.core :as reagent :refer [atom]]
            [trumpet.sound :as sound]
            [clojure.set :as cset]))

(enable-console-print!)

;; -- application state --

(def app-state
  (atom {:current-key :Gmaj}))

;; -- data --

(def Bb-transpositions
  "Key transpositions for Bb instruments (major second)"
  {:Abmaj :Bbmaj
   :Amaj  :Bmaj
   :Bbmaj :Cmaj
   :Bmaj  :Dbmaj
   :Cmaj  :Dmaj
   :Dbmaj :Ebmaj
   :Dmaj  :Emaj
   :Ebmaj :Fmaj
   :Emaj  :F#maj
   :Fmaj  :Gmaj
   :F#maj :Abmaj
   :Gmaj  :Amaj})

(def key-names
  {:Abmaj "G♯/A♭ maj"
   :Amaj  "A maj"
   :A#maj "A♯/B♭ maj"
   :Bbmaj "A♯/B♭ maj"
   :Bmaj  "B maj"
   :Cmaj  "C maj"
   :C#maj "C♯/D♭ maj"
   :Dbmaj "C♯/D♭ maj"
   :Dmaj  "D maj"
   :D#maj "D♯/E♭ maj"
   :Ebmaj "D♯/E♭ maj"
   :Emaj  "E maj"
   :Fmaj  "F maj"
   :F#maj "F♯/G♭ maj"
   :Gbmaj "F♯/G♭ maj"
   :Gmaj  "G maj"
   :G#maj "G♯/A♭ maj"})

(def notes
  {:Ab "A♭"
   :A  "A"
   :A# "A♯"
   :Bb "B♭"
   :B  "B"
   :B# "B♯"
   :Cb "C♭"
   :C  "C"
   :C# "C♯"
   :Db "D♭"
   :D  "D"
   :D# "D♯"
   :Eb "E♭"
   :E  "E"
   :E# "E♯"
   :Fb "F♭"
   :F  "F"
   :F# "F♯"
   :Gb "G♭"
   :G  "G"
   :G# "G♯"})

(def fingering
  {:Ab "⚪⚫⚫"
   :A  "⚫⚫⚪"
   :A# "⚫⚪⚪"
   :Bb "⚫⚪⚪"
   :B  "⚪⚫⚪"
   :C  "⚪⚪⚪"
   :C# "⚫⚫⚫"
   :Db "⚫⚫⚫"
   :D  "⚫⚪⚫"
   :D# "⚪⚫⚫"
   :Eb "⚪⚫⚫"
   :E  "⚫⚫⚪"
   :F  "⚫⚪⚪"
   :F# "⚪⚫⚪"
   :Gb "⚪⚫⚪"
   :G  "⚪⚪⚪"
   :G# "⚪⚫⚫"})

(def scales
  {:Amaj  [:A :B :C# :D :E :F# :G# :A]
   :Bmaj  [:B :C# :D# :E :F# :G# :A# :B]
   :Bbmaj [:Bb :C :D :Eb :F :G :A :Bb]
   :Cmaj  [:C :D :E :F :G :A :B :C]
   :C#maj [:C# :D# :E# :F# :G# :A# :B# :C#]
   :Dmaj  [:D :E :F# :G :A :B :C# :D]
   :Dbmaj [:Db :Eb :F :Gb :Ab :Bb :C :Db]
   :Ebmaj [:Eb :F :G :Ab :Bb :C :D :Eb]
   :Emaj  [:E :F# :G# :A :B :C# :D# :E]
   :Fmaj  [:F :G :A :Bb :C :D :E :F]
   :F#maj [:F# :G# :A# :B :C# :D# :E# :F#]
   :Gmaj  [:G :A :B :C :D :E :F# :G]
   :Abmaj [:Ab :Bb :C :Db :Eb :F :G :Ab]})

;; -- components --
(defn key-name-select-option
  "A dropdown box option for a particular musical key"
  [state idx k]
  ^{:key (str "key-name-" idx)}
  [:option {:value k} (key-names k)])

(defn concert-pitch-key-selector
  "Component to select the concert pitch key"
  [state]
  [:div.concert-pitch
   [:h1 "Concert Pitch"]
   [:select {:style {:font-size "1.2em"}
             :value (:current-key @state)
             :onChange #(swap! state assoc :current-key (keyword (.. % -target -value)))}
    (doall
     (map-indexed (partial key-name-select-option state)
                  (sort (keys Bb-transpositions))))]])

(defn transposed-pitch
  "Component that shows transposed pitch"
  [state]
  (let [transposed (key-names (Bb-transpositions (:current-key @state)))]
    [:div.transposed
     [:h1 "Transposed: " [:span transposed]]]))

(defn note-cell
  [idx note]
  ^{:key (str "note-" idx)}
  [:th {:onClick #(sound/play (sound/octave note 4))
        :style {:text-align "center"
                :font-weight "bold"
                :padding "4px"
                :background "#333"
                :color "#efefef"
                :width "12.5%"
                :border "1px solid #000"}} (notes note)])

(defn note-row
  [tonic]
  [:tr
   (doall (map-indexed note-cell (tonic scales)))])

(defn finger-cell
  [idx note]
  ^{:key (str "fingering-" idx)}
  [:td {:style {:text-align "center"
                :padding "12px 4px"
                :width "12.5%"
                :border "1px solid #000"}} (fingering note)])
(defn finger-row
  [tonic]
  [:tr
   (doall (map-indexed finger-cell (tonic scales)))])

(defn scale-table
  [state]
  (let [tonic (Bb-transpositions (:current-key @state))]
    [:table {:style {:width "100%" :font-size "1.2em"
                     :border-collapse "collapse"}}
     [note-row tonic]
     [finger-row tonic]]))

(defn title
  []
  [:h1.title {:style {:padding "10px"
                       :font-size "2.5em"
                       :font-weight "bold"
                       :border-bottom "1px solid #ccc"}} "B♭ Trumpet Transposed Scale / Fingering Chart"])

(defn copyright
  []
  [:footer
   [:p (str "Copyright ©" (.getFullYear (js/Date.))  " Christian Romney and Sebastian Romney. ")
    [:a {:href "https://opensource.org/licenses/MIT"} "MIT License."] " "
    [:span "Source code available on " [:a {:href "https://github.com/christianromney/trumpet"} "Github."]]]])

(defn Bb-transposition
  "This component renders a Bb transposed scale for
  the selected concert pitch "
  []
  [:div.key-selector {:style {:width "80%"
                              :margin "0 auto"
                              :padding "1em"
                              :font-family "Roboto, sans-serif"}}
   [title]
   [concert-pitch-key-selector app-state]
   [transposed-pitch app-state]
   [scale-table app-state]
   [copyright]])

;; -- entrypoint --

(defn init []
  (reagent/render-component
   [Bb-transposition]
   (.getElementById js/document "container")))

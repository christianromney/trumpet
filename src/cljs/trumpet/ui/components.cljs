(ns trumpet.ui.components
  "This namespace contains the application's React
  components that render the user interface."
  (:require [reagent.core :as reagent]
            [trumpet.logic.sounds :as sounds]
            [trumpet.logic.scales :as scales]
            [trumpet.logic.instruments :as instrument]
            [cljs.pprint :refer [pprint]]))

;; -- components --

(defn instrument-selector
  "Renders a component that allows the user to select their
  instrument. Changing the current instrument will update
  the application state with the correct transposition."
  [state]
  [:select {:value (:instrument @state)
            :on-change
            (fn [e]
              (let [selected (.. e -target -value)]
                (swap! state assoc
                       :instrument selected
                       :transposition (get instrument/transpositions selected))))}
   (doall
    (map-indexed
     (fn [idx [instrument transposition]]
       ^{:key (str "instrument-" idx)}
       [:option {:value instrument}
        instrument]) instrument/transpositions))])

(defn scale-selector
  "Component to select a major or minor scale to render."
  [state]
  (let [make-scale (fn [which]
                     (fn [coll]
                       (into coll
                             (mapv #(str (name %1) %2) scales/note-names (repeat which)))))
        maj-scale-fn (make-scale "maj")
        min-scale-fn (make-scale "min")
        separator    "---"
        scale-list (-> []
                       maj-scale-fn
                       (into [separator])
                       min-scale-fn)]

    [:select {:value (:scale @state)
              :on-change (fn [e]
                           (let [selected (.. e -target -value)]
                             (when-not (= selected separator)
                               (swap! state assoc :scale selected))))}
     (doall
      (map-indexed
       (fn [idx scale-name]
         (let [opts (if (= separator scale-name) {:disabled true} {})]
           ^{:key (str "scale-option-" idx)}
           [:option (merge opts {:value scale-name}) scale-name])) scale-list))]))

(defn octave-selector
  "Component to select in which octave the notes should be played."
  [state]
  [:select {:value (:octave @state)
            :on-change #(swap! state assoc :octave (js/parseInt (.. % -target -value)))}
   (doall
    (map
     (fn [octave]
       ^{:key (str "octave-" octave)}
       [:option octave]) (range scales/octaves)))])

(defn note-cell
  "Component which renders a single note in the scale table display.
  Clicking on the note name will cause the note to be sounded using
  HTML5 Audio."
  [state idx note]
  ^{:key (str "note-" idx)}
  [:th {:on-click #(sounds/play (name note))
        :style {:text-align "center"
                :font-weight "bold"
                :padding "4px"
                :background "#333"
                :color "#efefef"
                :width "12.5%"
                :border "1px solid #000"}} (scales/octave-note->note note)])

(defn note-row
  "Renders the row in the scale table that displays the note names."
  [state note-seq]
  [:tr
   (doall
    (map-indexed (partial note-cell state) note-seq))])

(defn finger-cell
  "Renders a fingering representation for each note of the current scale
  for the currently selected instrument, if such a fingering chart is
  available."
  [state idx note]
  ^{:key (str "fingering-" idx)}
  [:td {:style {:text-align "center"
                :padding "12px 4px"
                :width "12.5%"
                :border "1px solid #000"}}
   (get-in instrument/fingering
           [(:instrument @state)
            (scales/octave-note->note note)])])

(defn finger-row
  "Renders the row in the scale table that displays instrument fingerings."
  [state note-seq]
  [:tr
   (doall (map-indexed (partial finger-cell state) note-seq))])

(defn scale-table
  "Renders a table containing the notes and fingerings for the currently selected
  major or minor scale for the chosen instrument."
  [state]
  (let [scale+type (name (:scale @state))
        type-idx   (- (count scale+type) 3)
        scale      (keyword (.substring scale+type 0 type-idx))
        scale-type (keyword (.substring scale+type type-idx))
        scale-fn   (scales/scale-type->fn scale-type)
        note-seq   (scale-fn scale (:octave @state) (:transposition @state))]
    [:table {:style {:width "100%" :font-size "1.2em"
                     :border-collapse "collapse"}}
     [:tbody
      [note-row state note-seq]
      [finger-row state note-seq]]]))

(defn application-header
  "Renders the title of the application"
  [state]
  [:h1.title {:style {:padding "10px"
                       :font-size "2.5em"
                       :font-weight "bold"
                      :border-bottom "1px solid #ccc"}}
   [:span "Scale, Transposition, and Fingering Chart"]])

(defn application-footer
  "Renders the application copyright notice."
  [state]
  [:footer
   [:p (str "Copyright ©" (.getFullYear (js/Date.))  " Christian Romney and Sebastian Romney. ")
    [:a {:href "https://opensource.org/licenses/MIT"} "MIT License."] " "
    [:span "Source code available on " [:a {:href "https://github.com/christianromney/trumpet"} "Github."]]]])

(defn main
  "This component renders all of the application's tools."
  [state]
  [:div.key-selector {:style {:width "80%"
                              :margin "0 auto"
                              :padding "1em"
                              :font-family "Roboto, sans-serif"}}
   [application-header state]
   [instrument-selector state]
   [scale-selector state]
   [octave-selector state]
   [scale-table state]
   [application-footer state]])

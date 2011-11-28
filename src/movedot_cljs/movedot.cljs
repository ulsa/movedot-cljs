(ns jaypoop-cljs.movedot
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.graphics :as graphics]
            [goog.events.KeyHandler.EventType :as event-type]
            [goog.events.KeyCodes :as key-codes]))

;; define the colors for the squares and the dot
(def square-fill (graphics/SolidFill. "yellow"))
(def square-stroke (graphics/Stroke. 2 "green"))
(def dot-fill (graphics/SolidFill. "blue"))
(def dot-stroke (graphics/Stroke. 1 "black"))

;; the dot's initial position
(def dot (atom {:x 1 :y 1}))

;; properties
(def size 40)
(def margin 5)
(def width (- size margin))
(def num-rows 3)
(def num-cols 4)

(def g (doto (graphics/createGraphics "200" "150")
         (.render (dom/getElement "shapes"))))

;; call if the dot's position changes
(defn redraw-dot []
  (let [{:keys [x y graphics]} @dot
        hw (/ width 2)
        x-pos (+ margin (* x size) hw)
        y-pos (+ margin (* y size) hw)]
    (.setCenter graphics x-pos y-pos)))

;; key event handler
(def key-handler (events/KeyHandler. js/document))
(defn on-key-event [e]
  (let [{:keys [x y]} @dot
        key (. e keyCode)]
    (cond
      (and (= key key-codes/UP) (pos? y)) (swap! dot update-in [:y] dec)
      (and (= key key-codes/RIGHT) (<= x (- num-cols 2))) (swap! dot update-in [:x] inc)
      (and (= key key-codes/DOWN) (<= y (- num-rows 2))) (swap! dot update-in [:y] inc)
      (and (= key key-codes/LEFT) (pos? x)) (swap! dot update-in [:x] dec)))
  (redraw-dot))

;; draw the squares
(doseq [x (range num-cols)
        y (range num-rows)]
  (let [x-pos (+ margin (* x size))
        y-pos (+ margin (* y size))]
    (.drawRect g x-pos y-pos width width square-stroke square-fill)))

;; draw the initial dot
(let [{:keys [x y]} @dot
      hw (/ width 2)
      x-pos (+ margin (* x size) hw)
      y-pos (+ margin (* y size) hw)
      qw (/ width 4)
      graphics (.drawEllipse g x-pos y-pos qw qw dot-stroke dot-fill)]
  (swap! dot assoc :graphics graphics))

;; put everything together
(defn ^:export main []
  (events/listen key-handler event-type/KEY on-key-event))

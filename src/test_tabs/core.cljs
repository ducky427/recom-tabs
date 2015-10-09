(ns ^:figwheel-always test-tabs.core
  (:require [clojure.set :as cset]
            [reagent.core :as reagent]
            [re-com.core :as rcc]))

(enable-console-print!)

(defonce app-state (reagent/atom :a))

(def TRANSITION-DURATION 150)

(defn Tabs
  [tabs model on-change]
  (let [ids     (map :id tabs)
        states  (reagent/atom (into {} (map
                                        (fn [x]
                                          (if (= model x)
                                            {x "active in"}
                                            {x ""}))
                                        ids)))]
    (fn [tabs model on-change]
      [rcc/v-box
       :children [[rcc/horizontal-tabs
                   :tabs tabs
                   :model model
                   :on-change (fn [x]
                                (on-change x)
                                (when-not (= x model)
                                  (swap! states assoc model "")
                                  (js/setTimeout #(swap! states assoc x "active in")
                                                 TRANSITION-DURATION)))]
                  [rcc/v-box
                   :class "tab-content"
                   :children (doall
                              (for [t  tabs
                                    :let [k (:id t)]]
                                ^{:key k}
                                [rcc/box
                                 :class (str "tab-pane fade " (k @states))
                                 :child (:content t)]))]]])))

(defn hello-world
  []
  [rcc/v-box
   :margin "20px"
   :children [[rcc/title
               :label "Toggleable Tabs"
               :level :level1]
              [Tabs
               [{:id :a :label "A" :content [:p "Raw denim you probably haven't heard of them jean shorts Austin. Nesciunt tofu stumptown aliqua, retro synth master cleanse. Mustache cliche tempor, williamsburg carles vegan helvetica. Reprehenderit butcher retro keffiyeh dreamcatcher synth. Cosby sweater eu banh mi, qui irure terry richardson ex squid. Aliquip placeat salvia cillum iphone. Seitan aliquip quis cardigan american apparel, butcher voluptate nisi qui."]}
                {:id :b :label "B" :content [:p "Food truck fixie locavore, accusamus mcsweeney's marfa nulla single-origin coffee squid. Exercitation +1 labore velit, blog sartorial PBR leggings next level wes anderson artisan four loko farm-to-table craft beer twee. Qui photo booth letterpress, commodo enim craft beer mlkshk aliquip jean shorts ullamco ad vinyl cillum PBR. Homo nostrud organic, assumenda labore aesthetic magna delectus mollit. Keytar helvetica VHS salvia yr, vero magna velit sapiente labore stumptown. Vegan fanny pack odio cillum wes anderson 8-bit, sustainable jean shorts beard ut DIY ethical culpa terry richardson biodiesel. Art party scenester stumptown, tumblr butcher vero sint qui sapiente accusamus tattooed echo park."]}]
   @app-state
   (fn [x]
     (reset! app-state x))]]])

(reagent/render-component [hello-world] (. js/document (getElementById "app")))

(defn mount-root
  []
  (reagent/render [hello-world] (.getElementById js/document "app")))

(defn ^:export init!
  []
  (mount-root))
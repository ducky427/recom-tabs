(ns ^:figwheel-always test-tabs.core
  (:require [reagent.core :as reagent]
            [re-com.core :as rcc]))

(enable-console-print!)

(defonce app-state (reagent/atom :a))

;; https://github.com/twbs/bootstrap/blob/master/js/tab.js#L24
(def TRANSITION-DURATION 150)

(defn Tab
  ([content]
   [Tab content true])
  ([content is-active?]
   (let [animate  (reagent/atom false)]
     (fn [content is-active?]
       (if is-active?
         (js/setTimeout #(reset! animate true) TRANSITION-DURATION)
         (reset! animate false))
       [:div.tab-pane.fade
        {:class (when @animate
                  "active in")}
        content]))))

(defn Tabs
  [tabs model on-change]
  [rcc/v-box
   :children [[rcc/horizontal-tabs
               :tabs tabs
               :model model
               :on-change (fn [x]
                            (on-change x))]
              [:div.tab-content
               (for [t  tabs]
                 ^{:key (:id t)} [Tab (:content t) (= (:id t) model)])]
              [rcc/line]
              ^{:key model} [Tab [:p (name model)]]]])

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

(defn mount-root
  []
  (reagent/render [hello-world] (.getElementById js/document "app")))

(defn ^:export init!
  []
  (mount-root))

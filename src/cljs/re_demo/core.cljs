(ns re-demo.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-demo.events :as events]
   [re-demo.config :as config]))

;; Subs:
(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

;; View:
(defn main-panel []
  (let [name (re-frame/subscribe [::name])]
    [:div
     [:h1 "Hello from " @name ", dear Reader!"]]))

;; Core:
(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))

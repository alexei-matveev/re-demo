(ns re-demo.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-demo.db :as db]
   [re-demo.config :as config]))

;; Events:
(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

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
  (re-frame/dispatch-sync [::initialize-db])
  (dev-setup)
  (mount-root))

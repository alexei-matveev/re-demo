(ns re-demo.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]))

;; Config:
(def debug? ^boolean goog.DEBUG)

;; Events:
(re-frame/reg-event-db
 ::initialize-db
 ;; FIXME: The  two arguments are  ... The return value  is apparently
 ;; the initial value for the DB:
 (fn [_ _] {:name "Default Text"}))

;; Subs:
(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

;; View:
(defn main-panel []
  (let [name (re-frame/subscribe [::name])]
    [:div
     [:h1 "Hello from " @name ", Dear Reader!"]]))

;; Core:
(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

;; This  procedure  is  mentionend  by the  name  in  project.clj  for
;; on-jsload event, albeit only for dev build profile with figwheel:
(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [main-panel]
                  (.getElementById js/document "app")))

;; This appears to be called as  well from FIXME --- it does initialze
;; DB after all ...
(defn ^:export init []
  (re-frame/dispatch-sync [::initialize-db])
  (dev-setup)
  (mount-root))

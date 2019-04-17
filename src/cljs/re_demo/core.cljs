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

;;
;; ### Views ###
;;

;;
;; Collapsible Text with HTML5 [1]. Needs a lot of CSS voodoo, see
;; public/css in the resources dir.
;;
;; [1] https://alligator.io/css/collapsible/
;;
(defn collapsible []
  [:div.wrap-collapsible
   [:input.toggle {:type "checkbox" :id "xxx"}]
   [:label.lbl-toggle {:for "xxx"} "Clickme!"]
   [:div.collapsible-content
    [:div.content-inner
     [:p "Lorem ipsum delirium, memento mori!"]]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::name])]
    [:div
     [:h1 "Hello from " @name ", Dear Reader!"]
     [collapsible]]))

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

;; This appears  to be called  as well  from the script  in index.html
;; under static resources --- it does initialze DB after all ...
(defn ^:export init []
  (re-frame/dispatch-sync [::initialize-db])
  (dev-setup)
  (mount-root))

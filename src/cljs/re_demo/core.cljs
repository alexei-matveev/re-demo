(ns re-demo.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]))

;; Config:
(def debug? ^boolean goog.DEBUG)

;;
;; ### Events ###
;;

;;
;; Two functions reg-even-db and reg-event-fx should not be confused!
;;
;; [1] https://github.com/Day8/re-frame/blob/master/docs/Coeffects.md
(re-frame/reg-event-db
 ;; The first argument of the registered "coeffect" handler appears to
 ;; be the DB  itself.  The second is the event.   The return value is
 ;; apparently the DB, in this case its initial value:
 ::initialize-db (fn [db _] {:name "Default Text"}))

(re-frame/reg-event-db
 ;; The keyword ist  the leading entry of the event  vector. The event
 ;; ist the whole vector ...
 ::random-click (fn [db e] (assoc db :name (second e))))

;;
;; ### Subscriptions ###
;;
(re-frame/reg-sub
 ::name (fn [db] (:name db)))

;;
;; ### Views ###
;;

;;
;; Collapsible Text with HTML5 [1]. Needs a lot of CSS voodoo, see
;; public/css in the resources dir.
;;
;; [1] https://alligator.io/css/collapsible/
;;
(defn collapsible [id text]
  [:div.wrap-collapsible
   [:input.toggle {:type "checkbox" :id id}]
   [:label.lbl-toggle {:for id} "Clickme!"]
   [:div.collapsible-content
    [:div.content-inner
     [:p text]]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::name])]
    [:div
     [:h1 "Hello from " @name ", Dear Reader!"]
     [:div {:on-click #(re-frame/dispatch [::random-click "New Name"])}
      [:p "Try clicking on this par text ..."]]
     [collapsible "coll-1"
      "Lorem ipsum delirium, memento mori!"]
     [:p "Text inbetween ..."]
     [collapsible "coll-2"
      "Another hidden text with different ID."]]))

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

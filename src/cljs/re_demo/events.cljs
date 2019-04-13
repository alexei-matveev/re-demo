(ns re-demo.events
  (:require
   [re-frame.core :as re-frame]
   [re-demo.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

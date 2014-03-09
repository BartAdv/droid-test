(ns foo.droid.receiver
  (:gen-class
   :name foo.droid.GcmBroadcastReceiver
   :extends android.content.BroadcastReceiver)
  (:use [neko.threading :only [on-ui]]
        [neko.notify :only [toast]])
  (:require [neko.log :as log])
  (:import android.app.Activity
           android.content.Intent
           com.google.android.gms.gcm.GoogleCloudMessaging))

(defn -onReceive [this context intent]
  (log/i "receive!!!")
  (let [gcm (GoogleCloudMessaging/getInstance context)
        extras (.getExtras intent)
        msg (.getString extras "foo")]
    (log/i msg)
    (on-ui (toast msg))
    (.setResultCode this Activity/RESULT_OK)))

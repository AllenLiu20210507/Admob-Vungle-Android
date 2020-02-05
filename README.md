# Admob Android Mediation with Vungle SDK
This is a sample project by using mediation Mopub include Ad-Network Vungle.

## Goal
This project may help to reproduce issues and narrow down the root cause.

## Getting Started
- Download the Adapter
    Due to Vungle SDK Adapter is not on maven, you have to download [the adapter](https://vungle2-cdn-prod.s3.amazonaws.com/sdks/android/early-access/mopub/mopub-vungle-adapter-6_5_0-early.aar) and copy to the project's libs folder.

- Input your ad unit ids

    Create a file adunits.xml under the folder inlucde the content below with your Admob ids:

    ```
    <resources>
    <string name="ad_unit_id">Your_ad_unit_id</string>
    <string name="interstitial_placement_id">Your_interstitial_placement_id</string>
    <string name="reward_placement_id">Your_reward_placement_id</string>
    <string name="mrec_placement_id">Your_mrec_placement_id</string>
    <string name="banner_placement_id">Your_banner_placement_id</string>
    </resources>
    ```

## For more details, please check Mopub integration document
https://developers.mopub.com/publishers/ios/integrate/

## Tested Version

Admob: 
5.10.0

Vungle + Adapter: 
6.4.11 + 6.4.11.1


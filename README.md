# Admob Android Mediation with Vungle SDK
This is a sample project by using mediation Admob include Ad-Network Vungle.

## Goal
This project may help to reproduce issues and narrow down the root cause.

## Getting Started

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

## Tested Version

Admob: 
17.4.1

Vungle + Adapter: 
6.4.11 + 6.4.11.1


# Admob Android Mediation with Vungle SDK
This is a sample project by using mediation Admob include Ad-Network Vungle.

## Goal
This project may help to reproduce issues and narrow down the root cause.

## Getting Started

- Input your ad unit ids

    Add your Admob ids in String.xml:

    ```
    <string name="app_name">AdmobVungle</string>
    <string name="app_id">Your_app_id</string>
    <string name="interstitial_placement_id">Your_interstitial_placement_id</string>
    <string name="reward_placement_id">Your_reward_placement_id</string>
    <string name="mrec_placement_id">Your_mrec_placement_id</string>
    <string name="banner_placement_id">Your_banner_placement_id</string>
    ```
## Tips:
- Due to the 6.5.2 adapter is not released yet from Admob,you need to download the admob-vungle-adapters-6.5.2.0.aar form [https://support.vungle.com/hc/en-us/articles/360027517832-Early-Access-Integrating-AdMob-Vungle-SDK-v-6-5-2-Android-]

## Tested Version

Admob: 
7.53.1

Vungle + Adapter: 
6.5.2 + 6.5.2(early access)


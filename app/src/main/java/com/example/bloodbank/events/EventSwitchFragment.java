package com.example.bloodbank.events;

public class EventSwitchFragment {
    int fragmentTag;

    public EventSwitchFragment(int fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public int getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentName(int fragmentName) {
        this.fragmentTag = fragmentTag;
    }
}

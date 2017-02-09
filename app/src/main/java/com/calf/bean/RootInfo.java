package com.calf.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JinYi Liu on 17-2-9.
 */

public class RootInfo {

    private List<SectionInfo> mSections = new ArrayList<SectionInfo>();

    public List<SectionInfo> getSections() {
        return mSections;
    }

    public int getSectionSize() {
        return mSections.size();
    }

    public void addSection(SectionInfo section) {
        if (section != null) {
            this.mSections.add(section);
        }
    }

    public SectionInfo getFirstSection() {
        if (getSectionSize() > 0) {
            return mSections.get(0);
        }
        return null;
    }

    public SectionInfo getLastSection() {
        if (mSections.size() > 0) {
            return mSections.get(mSections.size() - 1);
        }
        return null;
    }

}

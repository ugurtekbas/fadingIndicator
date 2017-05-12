package com.ugurtekbas.fadingindicatorlibrary;

/**
 * An interface for responding when active page is changed
 * @author Ugur Tekbas
 * on 12/05/17.
 */

public interface PageChangedListener {

    /**
     * This method will be invoked when a new page becomes active, when user slides
     * from one page to another
     * @param pageIndex position of the active page on list of pages
     */
    void onPageFlipped(int pageIndex);

}

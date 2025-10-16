package adapterviewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ca.useful.nfcclient.R;
import connector.ConnectorExampleE;
import fragments.FragmentExampleA;

public class ViewPagerExampleEStateAdapter extends FragmentStatePagerAdapter {

    private final ConnectorExampleE connectorExampleE;

    private FragmentExampleA fragmentExampleA;
    private FragmentExampleA fragmentExampleAPage2;

    public ViewPagerExampleEStateAdapter(@NonNull FragmentManager fm, ConnectorExampleE connectorExampleE) {
        super(fm);
        this.connectorExampleE = connectorExampleE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (fragmentExampleA == null) {
                    fragmentExampleA = FragmentExampleA.newInstance();
                }

                return fragmentExampleA;
            case 1:
                if (fragmentExampleAPage2 == null) {
                    fragmentExampleAPage2 = FragmentExampleA.newInstance();
                }

                return fragmentExampleAPage2;
            default:
                //return a default view fragment
        }
        return null;
    }



    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return connectorExampleE.processResourceIdToString(R.string.page_one_string_adapter_example_e);
            case 1:
                return connectorExampleE.processResourceIdToString(R.string.page_two_string_adapter_example_e);

            default:
                return "";
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
//        return POSITION_NONE;
        Fragment f = (Fragment) object;
        for (int i = 0; i < getCount(); i++) {
            Fragment item = (Fragment) getItem(i);
            if (item.equals(f)) {
                return i;
            }
        }
        return POSITION_NONE;
    }

}

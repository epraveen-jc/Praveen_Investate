package com.example.praveen_investate.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praveen_investate.R;
import com.example.praveen_investate.adapter.PropertyTypeAdapter;
import com.example.praveen_investate.model.PropertyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PropertyTypeAdapter adapter;
    private TextView title;
    private SearchView searchView;
    private List<PropertyType> propertyTypes;
    private List<PropertyType> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerview);
        title = findViewById(R.id.title);
        searchView = findViewById(R.id.searchView_forcat);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Initialize property types list
        propertyTypes = Arrays.asList(
                new PropertyType("Apartment", R.drawable.apartment),
                new PropertyType("Independent_House", R.drawable.indipendenthouse),
                new PropertyType("Plot", R.drawable.plots),
                new PropertyType("Villa", R.drawable.villa),
                new PropertyType("Farmhouse", R.drawable.farmhouse),
                new PropertyType("Commercial", R.drawable.commercial),
                new PropertyType("Gated_Community", R.drawable.gated),
                new PropertyType("Rental_House", R.drawable.rent),
                new PropertyType("Agricultural Land", R.drawable.agriculture)
        );

        complexLogicForSearch();


        // Initialize the adapter with the full property list
        filteredList = new ArrayList<>(propertyTypes);
        adapter = new PropertyTypeAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        // Set up search functionality
        setupSearchView();
    }
    private void complexLogicForSearch(){
        //apartment
        propertyTypes.get(0).setKeywords(
                "apartment, appartmnt, appartment, aprtment, apartmnt, aparment, aprtmnt, apartmnt, appartament, aparment, aptmnt, aparttment, aprtmnt, apartmant, appartment, aparment, aprtmnt, apartmentss, apptmnt, apartmnts, appartmnt, aparament, apartament, apartement, apparment, apartmant, apptment, apatmment, apptmnt, apartmeent, apparment, apatmment, apartmente, aparmment, aparttment, aprtment, appartment, apartmnts, appartmentts, appartament, appartmentt, aparrtment, appatmment, appartmentt, aptmnts, aprtmment, apartemnt, appartaments, apparmentts, aprtmnts"
        );
        // ind house
        propertyTypes.get(1).setKeywords(
                "independent house, independnt house, indepndnt house, independant house, indepent house, indpendent house, independant hs, indepndent house, independant hse, independanthouse, indpendant hs, indepentent house, independt house, indepdent house, indpendant house, indpendent hse, independnt hse, indepenent house, independnt hs, indpenent house, independnt hoouse, indpendant hs, indepent hs, independ house, independnt hoouse, indepent hs, indpendenthouse, indep house, indpendent hs, independt hs, indpend house, indepdnt house, indpend hs, indepent hse, independet house, indepedent house, indepenent hs, indepenent hse, indepandent house, indepndant house, independent huse, independant hsue, indepandant house, indepenedent house, independet hs, indepdent hse, indepndent hse, independ hse, indepeendent house."
        );
        //plot
        propertyTypes.get(2).setKeywords(
                "plot, plott, plt, ploat, pllott, plott, ploott, plotts, plotz, pplot, ploot, pllt, ploott, ploat, p-lot, ploot, ppplot, ploott, ploat, ploting, plotter, plots, plote, plort, polt, plottz, p-lots, platt, p-lote, ploty, p-lotz, pltz, pltot, poltt, p-lt, ploty, pp-lot, plotter, p-lote, plor, ploat, polot, p-lt, p-lt, ploatt, plont, pllott, ploott, polat, ploot.\n"
        );
        //villa
        propertyTypes.get(3).setKeywords(
                "villa, vlla, vila, villla, villaa, viila, vvilla, villah, vill, v-illa, villar, viilla, villahs, vilaa, vills, villah, vvlla, vla, vlla, villaa, villahh, villha, villarh, vilha, villl, villlas, villaas, villaah, v-ila, villhas, villaa, vllla, vllah, villae, villaas, villha, vlla, vlla, vlah, viilah, v-llla, viilha, villlaas, villahs, vllas, viilaa, villlas, villaas, v-ilas.\n"
        );
        //farmhouse
        propertyTypes.get(4).setKeywords(
                "farmhouse, farmhse, frmhouse, frmhse, farmehouse, farmhous, frmhous, farmhouze, farmhousez, farhouse, frmhouz, farmhose, frmhose, farmhause, farmhowse, farhmhouse, farmhouuse, faarmhouse, farmhousz, farhmhse, farmhousee, frmhose, farmehoose, frmhouuse, frmhuse, farmhouss, farmhousees, farmhousse, farmmhouse, f-armhouse, fmhse, farmhosue, farhmouse, frmhoose, f-armhouz, far-mhouse, frmhousee, farmhuose, far-mhse, ffarmhouse, frhouse, farmhouxe, far-hmhouse, frhousee, frmhouz, fr-huse, f-armhose, farmhooz, frmhuse, ffarmhouze.\n"
        );
        //Commercial
        propertyTypes.get(5).setKeywords(
                "commercial, shopping mall ,commrcial, commerical, commercl, commercal, commerial, commerchal, comrcial, comercial, commrcl, commrchl, commercil, comrcial, commeriall, commercal, commerical, commercll, commerchal, commerxial, commrcail, commrical, commercel, commerical, commercal, cmmrcial, comerial, commmrcial, commericall, commrcl, commrchial, commerzial, commerial, cmmercial, commerchil, c-ommercial, commerisial, commericeal, commrchal, commercail, commersial, commearcial, commrciall, commerchial, commerisial, com-mercial, commeriscial, commer-cial, commerrcial, commerse, comerial.\n"
        );
        //gated community
        propertyTypes.get(6).setKeywords(
                "gated community, gatd community, gatted community, gated communty, gatted comunity, gated communitty, gatd comunity, gatted commnity, gated comunity, gated comunnity, gatd comnity, gate community, gatted comunitee, gated communtiy, gatd comunitee, gated comunite, gatted communitee, gated commnity, gatted communitty, gatted communty, gated comnty, gated comunittie, gatted commuity, gated commuity, gatedcomunity, gatedcommuntiy, gattedcomunity, gted community, gted comnity, gattd community, gate comunity, gatted comm, gated communtee, gattedcomunitee, gated communtee, gted comunitee, gatedcommnity, gated communiti, gatd communiti, gatedcommuntie, gatedcommnity, gtted comunity, gatedcomm, gatedcomun, gatedcommunitie, gated-communitty, gated-communitie, gated-com.\n"
        );
        //rental house
        propertyTypes.get(7).setKeywords(
                "rental house, rental hse, rental hs, rental houze, rentl house, rentl hse, rent house, renatl house, rntl house, renatl hse, renatl hs, rentl hse, rentl hs, rentl houze, rentel house, rent-al house, rentel hs, renatl houze, ren-tal house, rentl hs, to let, to-let, tolet, to-lat, tollet, to-late, to lat, toalete, toolat, tulat, tolte, t-olate, tool-ate, two let, toalette, 2 let, tu let, tu-late, t-lett, to-lett, to lett, tollete, toll-et, 2-let, two-lat, tool-lat, t-tolet, two-late, t-olet, to-lettee, toollette , tolate to-late ,to-let , to let to late.\n"
        );
        //agriculture land
        propertyTypes.get(8).setKeywords(
                "agriculture land, agriculure land, agricultural land, agrculture land, agriultural land, agricultral land, agri land, agric land, agricultre land, agri land, agricultur land, agriclture land, agricultual land, agrilculture land, agriculturalland, agricltural land, agiculture land, agriculand, agricultreland, agriulturalland, agri land, agriland, agriultural land, agri-culture land, agricuture land, agricutural land, agri-culture, agricluture land, agrcultural land, agrcltural land, agri-culturalland, agrculture land, agriculturland, agri-lculture land, agricultral, agriculturalland, agri culture land, agrt land, agrilnd land, agrcltureland, agri-culturalland, agricutland, agriculureland, agricultralnd, agr-cultureland, agri-culturalland, agriultre land, agri-cultur.\n"
        );
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPropertyTypes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPropertyTypes(newText);
                return true;
            }
        });
    }

    private void filterPropertyTypes(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(propertyTypes);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (PropertyType property : propertyTypes) {
                if (property.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(property);
                }else if(property.getKeywords().toLowerCase().contains(lowerCaseQuery)){
                    filteredList.add(property);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}

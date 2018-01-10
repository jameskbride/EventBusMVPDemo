package com.jameskbride.eventbusmvpdemo.main;

import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFactory;
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;
import com.jameskbride.eventbusmvpdemo.security.SecurityErrorViewFactory;
import com.jameskbride.eventbusmvpdemo.security.SecurityErrorViewFragment;
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MainActivityImplTest {

    @Mock private MainActivity mainActivity;
    @Mock private MainActivityPresenter presenter;
    @Mock private ToasterWrapper toasterWrapper;
    @Mock private EditText profileIdEditText;
    @Mock private Editable editable;
    @Mock private Button submitButton;
    @Mock private TextView customerName;
    @Mock private TextView addressLine1;
    @Mock private TextView addressLine2;
    @Mock private TextView city;
    @Mock private TextView state;
    @Mock private TextView zipCode;
    @Mock private LinearLayout noOrdersBlock;
    @Mock private LinearLayout foundOrdersBlock;
    @Mock private ListView orders;
    @Mock private ArrayAdapter<String> ordersAdapter;
    @Mock private OrdersAdapterFactory ordersAdapterFactory;
    @Mock private View view;
    @Mock private NetworkErrorViewFragment networkErrorViewFragment;
    @Mock private SecurityErrorViewFragment securityErrorViewFragment;
    @Mock private NetworkErrorViewFactory networkErrorViewFactory;
    @Mock private SecurityErrorViewFactory securityErrorViewFactory;

    @Mock private FragmentManager fragmentManager;

    private MainActivityImpl subject;

    @Before
    public void setUp() {
        initMocks(this);

        subject = new MainActivityImpl(presenter);
        subject.toasterWrapper = toasterWrapper;
        subject.ordersAdapterFactory = ordersAdapterFactory;
        subject.networkErrorViewFactory = networkErrorViewFactory;
        subject.securityErrorViewFactory = securityErrorViewFactory;

        when(mainActivity.findViewById(R.id.profile_id_edit)).thenReturn(profileIdEditText);
        when(profileIdEditText.getText()).thenReturn(editable);
        when(mainActivity.findViewById(R.id.submit)).thenReturn(submitButton);
        when(mainActivity.findViewById(R.id.customer_name)).thenReturn(customerName);
        when(mainActivity.findViewById(R.id.address_line_1)).thenReturn(addressLine1);
        when(mainActivity.findViewById(R.id.address_line_2)).thenReturn(addressLine2);
        when(mainActivity.findViewById(R.id.city)).thenReturn(city);
        when(mainActivity.findViewById(R.id.state)).thenReturn(state);
        when(mainActivity.findViewById(R.id.zipcode)).thenReturn(zipCode);
        when(mainActivity.findViewById(R.id.no_orders_block)).thenReturn(noOrdersBlock);
        when(mainActivity.findViewById(R.id.found_orders_block)).thenReturn(foundOrdersBlock);
        when(mainActivity.findViewById(R.id.order_list)).thenReturn(orders);
        when(mainActivity.findViewById(android.R.id.content)).thenReturn(view);
        when(mainActivity.getSupportFragmentManager()).thenReturn(fragmentManager);

        subject.onCreate(null, mainActivity);
    }

    @Test
    public void onCreateSetsTheContentView() {
        verify(mainActivity).setContentView(R.layout.activity_main);
    }

    @Test
    public void onCreateConfiguresTheSubmitButton() {
        when(editable.toString()).thenReturn("2");
        subject.onCreate(null, mainActivity);
        ArgumentCaptor<View.OnClickListener> onClickCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);

        verify(submitButton, atLeastOnce()).setOnClickListener(onClickCaptor.capture());

        onClickCaptor.getValue().onClick(null);

        verify(presenter).getProfile("2");
    }

    @Test
    public void itCanDisplayAnErrorMessage() {
        when(toasterWrapper.makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG)).thenReturn(toasterWrapper);

        subject.displayError(R.string.oops);

        verify(toasterWrapper).makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG);
        verify(toasterWrapper).show();
    }

    @Test
    public void onResumeOpensThePresenter() {
        subject.onResume(mainActivity);

        verify(presenter).open();
    }

    @Test
    public void onPauseClosesThePresenter() {
        subject.onPause(mainActivity);

        verify(presenter).close();
    }

    @Test
    public void itDisplaysTheProfileDetailsOnGetProfileResponse() {
        ProfileResponse profileResponse = buildProfileResponseWithoutOrders();

        subject.displayProfileDetails(profileResponse);

        verify(customerName).setText(profileResponse.getFirstName() + " " + profileResponse.getLastName());
        verify(addressLine1).setText(profileResponse.getAddressLine1());
        verify(addressLine2).setText(profileResponse.getAddressLine2());
        verify(city).setText(profileResponse.getCity());
        verify(state).setText(profileResponse.getState());
        verify(zipCode).setText(profileResponse.getZipCode());
    }

    @Test
    public void itDisplaysOrdersWhenTheyAreAvailable() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();

        List<String> descriptions = new ArrayList<>();
        descriptions.add(profileResponse.getOrderHistory().get(0).getDescription());
        when(ordersAdapterFactory.make(
                mainActivity, android.R.layout.simple_list_item_1,
                descriptions)).thenReturn(ordersAdapter);

        subject.displayOrders(profileResponse.getOrderHistory());

        verify(foundOrdersBlock).setVisibility(View.VISIBLE);
        verify(noOrdersBlock).setVisibility(View.GONE);

        verify(orders).setAdapter(ordersAdapter);
    }

    @Test
    public void itDisplaysNoOrdersWhenOrdersAreNotAvailable() {
        subject.displayNoOrders();

        verify(foundOrdersBlock).setVisibility(View.GONE);
        verify(noOrdersBlock).setVisibility(View.VISIBLE);
    }

    @Test
    public void itCanDisplayTheNetworkErrorView() {
        NetworkRequestEvent networkRequestEvent = new NetworkRequestEvent();
        when(networkErrorViewFactory.make(networkRequestEvent)).thenReturn(networkErrorViewFragment);

        subject.displayNetworkError(new NetworkErrorEvent(networkRequestEvent));

        verify(networkErrorViewFragment).show(eq(fragmentManager), eq("networkError"));
    }

    @Test
    public void itCanDisplayTheSecurityErrorView() {
        when(securityErrorViewFactory.make()).thenReturn(securityErrorViewFragment);

        subject.displaySecurityError();

        verify(securityErrorViewFragment).show(eq(fragmentManager), eq("securityError"));
    }

    private ProfileResponse buildProfileResponseWithoutOrders() {
        ProfileResponse profileResponse = new ProfileResponse(
                "first name",
                "last name",
                "address line 1",
                "address line 2",
                "city",
                "OH",
                "12345",
                new ArrayList<Order>()
        );
        return profileResponse;
    }

    private ProfileResponse buildProfileResponseWithOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "description"));
        ProfileResponse profileResponse = new ProfileResponse(
                "first name",
                "last name",
                "address line 1",
                "address line 2",
                "city",
                "OH",
                "12345",
                orders
        );
        return profileResponse;
    }
}
package pebbleantivpn.pebbleantivpn;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;

public class Listener {

    @Subscribe(order = PostOrder.EARLY)
    public void onPlayerChat(LoginEvent e) {


    }

}

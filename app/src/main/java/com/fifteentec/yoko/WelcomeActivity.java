package com.fifteentec.yoko;

import android.os.Bundle;

import com.fifteentec.Component.User.UserServer;

public class WelcomeActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        UserServer.getInstance().autoLogin(WelcomeActivity.this);

        /*Friend friend1 = new Friend("makaixiong", "xijinping1", "no.1", "favorite1");
        Friend friend2 = new Friend("makaixiong", "xijinping2", "no.1", "favorite1");
        Friend friend3 = new Friend("makaixiong", "xijinping3", "no.2", "favorite2");
        Friend friend4 = new Friend("makaixiong", "xijinping3", "no.2", "favorite2");
        Friend friend5 = new Friend("makaixiong", "xijinping4", "no.3", "favorite3");
        List<Friend> friends = new ArrayList<Friend>();
        friends.add(friend1);
        friends.add(friend2);
        friends.add(friend3);
        friends.add(friend4);
        friends.add(friend5);

        dbManager.getFriend().addFriend(friend1);
        dbManager.getFriend().addFriends(friends);
        friends.clear();
        friends = dbManager.getFriend().queryAll();

        Friend newFriend1 = new Friend("makaixiong", "xijinping5", "no.5", "favorite5");
        dbManager.getFriend().updateFuid(newFriend1, friend1);
        friends.clear();
        friends = dbManager.getFriend().queryAll();
        dbManager.getFriend().updateTagId(newFriend1, friend1);
        friends.clear();
        friends = dbManager.getFriend().queryAll();
        dbManager.getFriend().updateTagName(newFriend1, friend1);
        friends.clear();
        friends = dbManager.getFriend().queryAll();

        Friend newFriend2 = new Friend("makaixiong", "xijinping6", "no.6", "favorite6");
        dbManager.getFriend().updateFriend(newFriend2, friend1);
        friends.clear();
        friends = dbManager.getFriend().queryAll();

        dbManager.getFriend().deleteFriend(friend3);
        friends.clear();
        friends = dbManager.getFriend().queryAll();

        dbManager.getFriend().addFriend(friend3);
        friends.clear();
        friends = dbManager.getFriend().queryAll();
        dbManager.getFriend().addFriend(friend4);
        friends.clear();
        friends = dbManager.getFriend().queryAll();

        dbManager.getFriend().deleteFriendTag(friend3);
        friends.clear();
        friends = dbManager.getFriend().queryAll();

        friends.clear();
        friends = dbManager.getFriend().queryFriend(friend3);

        friends.clear();
        friends = dbManager.getFriend().queryAll();

        dbManager.getFriend().clear();
        friends.clear();
        friends = dbManager.getFriend().queryAll();*/
    }
}

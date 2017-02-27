package com.example.thomas.scoutranktracker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import android.view.MenuItem;


public class Scout extends Activity {

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> regs;
    ExpandableListView expListView;
    Database db = new Database(this);

    boolean[] scoutTrac;
    boolean[] tenderfootTrac;
    boolean[] secondClassTrac;
    boolean[] firstClassTrac;
    boolean[] starTrac;
    boolean[] lifeTrac;
    boolean[] eagleTrac;
    int[] number;
    int rankNum;
    int ScoutNum;
    int TenderfoorNum;
    int SecondClassNum;
    int FirstClassNum;
    int StarNum;
    int LifeNum;
    int EagleNum;
    boolean rankRegs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);

        createRankList(); // Makes rank list
        createRegs();// Fills in requirements
        setupBool();//creating boolean array;
        if(db.tableExist()==false) {
            createDB();
        }
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, regs);
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                rankNum = getRegNum(groupPosition);

                rankRegs = getBool(groupPosition,childPosition);
                if(childPosition == 0) {
                    for (int i = 0; i < rankNum; i++) {
                        boolean temp = getBool(groupPosition, i);
                        if (temp == true) {
                            int j = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, i));
                            parent.setItemChecked(j, true);
                        }
                    }
                }

                if (rankRegs == false && childPosition != 0) { // adding a requirement to the table

                    if (childPosition != 0) {
                        parent.setItemChecked(index, true);
                        rankRegs= true;
                        fillBool(groupPosition,childPosition, true);
                        db.updateReg(groupPosition,rankRegs,childPosition);
                        int testing = db.numberOfRows();

                        Toast.makeText(getBaseContext(), "Checked OFF! testing= " + testing + " ", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getBaseContext(), "You can't Check the Check", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(rankRegs == true)// removing a requirement from the table
                {
                    index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                    parent.setItemChecked(index,false);
                    rankRegs = false;
                    fillBool(groupPosition,childPosition, false);
                    db.updateReg(groupPosition,rankRegs,childPosition);
                    Toast.makeText(getBaseContext(), "Guess you didn't do that one", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList, regs);
        expListView.setAdapter(expListAdapter);
        updateBool();
        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                rankNum = getRegNum(groupPosition);
                rankRegs = getBool(groupPosition,childPosition);
               if(childPosition == 0) {
                   for (int i = 0; i < rankNum; i++) {
                       boolean temp = getBool(groupPosition, i);
                       if (temp == true) {
                           int j = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, i));
                           parent.setItemChecked(j, true);
                       }
                   }
               }

                if (rankRegs == false && childPosition != 0) { // adding a requirement to the table

                    if (childPosition != 0) {
                        parent.setItemChecked(index, true);
                        rankRegs= true;
                        fillBool(groupPosition,childPosition, true);
                        db.updateReg(groupPosition,rankRegs,childPosition);
                        int testing = db.numberOfRows();

                        Toast.makeText(getBaseContext(), "Checked OFF! testing= " + testing + " ", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        db.updateReg(groupPosition,rankRegs,childPosition);
                        Toast.makeText(getBaseContext(), "You can't Check the Check", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(rankRegs == true)// removing a requirement from the table
                {
                    index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                    parent.setItemChecked(index,false);
                    rankRegs = false;
                    fillBool(groupPosition,childPosition, false);
                    db.updateReg(groupPosition,rankRegs,childPosition);
                    Toast.makeText(getBaseContext(), "Guess you didn't do that one", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createRankList() {
        groupList = new ArrayList<String>();
        groupList.add("Scout");
        groupList.add("Tenderfoot");
        groupList.add("Second Class");
        groupList.add("First Class");
        groupList.add("Star");
        groupList.add("Life");
        groupList.add("Eagle");
    }

    private void createRegs() {
        String[] scout = {"Click here to see what you have Checked off already\n",
                " 1a) Repeat from memory the Scout Oath, Scout Law, Scout motto, and Scout\n" +
                "slogan. In your own words, explain their meaning. ",
                " 1b) Explain what Scout spirit is. Describe some ways you have shown Scout spirit\n" +
                        "by practicing the Scout Oath, Scout Law, Scout motto, and Scout slogan.",
                " 1c) Demonstrate the Boy Scout sign, salute, and handshake. Explain when\n" +
                        "they should be used.",
                " 1d) Describe the First Class Scout badge and tell what each part stands for.\n" +
                        "Explain the significance of the First Class Scout badge.",
                " 1e) Repeat from memory the Outdoor Code. In your own words, explain what\n" +
                        "the Outdoor Code means to you.",
                " 1f) Repeat from memory the Pledge of Allegiance. In your own words, explain\n" +
                        "its meaning.",
                " 2) After attending at least one Boy Scout troop meeting, do the following: ",
                " 2a) Describe how the Scouts in the troop provide its leadership.",
                " 2b) Describe the four steps of Boy Scout advancement. ",
                " 2c) Describe what the Boy Scout ranks are and how they are earned.",
                " 2d) Describe what merit badges are and how they are earned. ",
                " 3a) Explain the patrol method. Describe the types of patrols that are used in\n" +
                        "your troop. ",
                " 3b) Become familiar with your patrol name, emblem, flag, and yell. Explain\n" +
                        "how these items create patrol spirit.",
                " 4a) Show how to tie a square knot, two half-hitches, and a taut-line hitch.\n" +
                        "Explain how each knot is used. ",
                " 4b) Show the proper care of a rope by learning how to whip and fuse the\n" +
                        "ends of different kinds of rope.",
                " 5) Demonstrate your knowledge of pocketknife safety. ",
                " 6) With your parent or guardian, complete the exercises in the pamphlet\n" +
                        "How to Protect Your Children From Child Abuse: A Parent’s Guide and\n" +
                        "earn the Cyber Chip Award for your grade.",
                " 7) Since joining the troop and while working on the Scout rank, participate in\n" +
                        "a Scoutmaster conference. "};

        String[] tenderfoot = {"Click here to see what you have Checked off already\n",
                " 1a) Present yourself to your leader, prepared for an overnight camping trip.\n" +
                "Show the personal and camping gear you will use. Show the right way to\n" +
                "pack and carry it. ",
                " 1b) Spend at least one night on a patrol or troop campout. Sleep in a tent you\n" +
                        "have helped pitch",
                " 1c) Tell how you practiced the Outdoor Code on a campout or outing. ",
                " 2a) On the campout, assist in preparing one of the meals. Tell why it is important\n" +
                        "for each patrol member to share in meal preparation and cleanup. ",
                " 2b) While on a campout, demonstrate the appropriate method of safely\n" +
                        "cleaning items used to prepare, serve, and eat a meal. ",
                " 2c) Explain the importance of eating together as a patrol. ",
                " 3a) Demonstrate a practical use of the square knot. ",
                " 3b) Demonstrate a practical use of two half-hitches. ",
                " 3c) Demonstrate a practical use of the taut-line hitch. ",
                " 3d) Demonstrate proper care, sharpening, and use of the knife, saw, and ax.\n" +
                        "Describe when each should be used. ",
                " 4a) Show first aid for the following:\n" +
                        "• Simple cuts and scrapes\n" +
                        "• Blisters on the hand and foot\n" +
                        "• Minor (thermal/heat) burns or scalds (superficial, or first-degree)\n" +
                        "• Bites or stings of insects and ticks\n" +
                        "• Venomous snakebite\n" +
                        "• Nosebleed\n" +
                        "• Frostbite and sunburn\n" +
                        "• Choking",
                " 4b) Describe common poisonous or hazardous plants; identify any that grow in\n" +
                        "your local area or campsite location. Tell how to treat for exposure to them. ",
                " 4c) Tell what you can do while on a campout or other outdoor activity to\n" +
                        "prevent or reduce the occurrence of injuries or exposure listed in Tenderfoot\n" +
                        "requirements 4a and 4b. ",
                " 4d) Assemble a personal first-aid kit to carry with you on future campouts and\n" +
                        "hikes. Tell how each item in the kit would be used. ",
                " 5a) Explain the importance of the buddy system as it relates to your personal\n" +
                        "safety on outings and in your neighborhood. Use the buddy system while\n" +
                        "on a troop or patrol outing. ",
                " 5b) Describe what to do if you become lost on a hike or campout. ",
                " 5c) Explain the rules of safe hiking, both on the highway and cross-country,\n" +
                        "during the day and at night. ",
                " 6a) Record your best in the following tests:\n" +
                        "• Pushups (Record the number done correctly in 60 seconds.)\n" +
                        "• Situps or curl-ups (Record the number done correctly in\n" +
                        "60 seconds.)\n" +
                        "• Back-saver sit-and-reach (Record the distance stretched.)\n" +
                        "• 1-mile walk/run (Record the time.)\n",
                " 6b) Develop and describe a plan for improvement in each of the activities\n" +
                        "listed in Tenderfoot requirement 6a. Keep track of your activity for at least\n" +
                        "30 days",
                " 6c) Show improvement (of any degree) in each activity listed in Tenderfoot\n" +
                        "requirement 6a after practicing for 30 days.\n" +
                        "• Pushups (Record the number done correctly in\n" +
                        "60 seconds.)\n" +
                        "• Situps or curl-ups (Record the number done correctly in 60\n" +
                        "seconds.)\n" +
                        "• Back-saver sit-and-reach (Record the distance stretched.)\n" +
                        "• 1-mile walk/run (Record the time.)\n",
                " 7a) Demonstrate how to display, raise, lower, and fold the U.S. flag. ",
                " 7b) Participate in a total of one hour of service in one or more service projects\n" +
                        "approved by your Scoutmaster. Explain how your service to others relates\n" +
                        "to the Scout slogan and Scout motto. ",
                " 8) Describe the steps in Scouting’s Teaching EDGE method. Use the Teaching\n" +
                        "EDGE method to teach another person how to tie the square knot. \n",
                " 9) Demonstrate Scout spirit by living the Scout Oath and Scout Law. Tell how\n" +
                        "you have done your duty to God and how you have lived four different\n" +
                        "points of the Scout Law in your everyday life. ",
                " 10) While working toward the Tenderfoot rank, and after completing Scout\n" +
                        "rank requirement 7, participate in a Scoutmaster conference. ",
                " 11) Successfully complete your board of review for the Tenderfoot rank."};

        String[] secondClass = {"Click here to see what you have Checked off already\n",
                " 1a) Since joining, participate in five separate troop/patrol activities, three of\n" +
                "which include overnight camping. These five activities do not include troop\n" +
                "or patrol meetings. On at least two of the three campouts, spend the night in\n" +
                "a tent that you pitch or other structure that you help erect (such as a lean-to,\n" +
                "snow cave, or tepee). ",
                " 1b) Explain the principles of Leave No Trace and tell how you practiced them\n" +
                        "on a campout or outing. This outing must be different from the one used for\n" +
                        "Tenderfoot requirement 1c. ",
                " 1c) On one of these campouts, select a location for your patrol site and\n" +
                        "recommend it to your patrol leader, senior patrol leader, or troop guide.\n" +
                        "Explain what factors you should consider when choosing a patrol site and\n" +
                        "where to pitch a tent. ",
                " 2a) Explain when it is appropriate to use a fire for cooking or other purposes\n" +
                        "and when it would not be appropriate to do so.",
                " 2b) Use the tools listed in Tenderfoot requirement 3d to prepare tinder,\n" +
                        "kindling, and fuel wood for a cooking fire. ",
                " 2c) At an approved outdoor location and time, use the tinder, kindling, and\n" +
                        "fuel wood from Second Class requirement 2b to demonstrate how to\n" +
                        "build a fire. Unless prohibited by local fire restrictions, light the fire. After\n" +
                        "allowing the flames to burn safely for at least two minutes, safely extinguish\n" +
                        "the flames with minimal impact to the fire site.",
                " 2d) Explain when it is appropriate to use a lightweight stove and when it is\n" +
                        "appropriate to use a propane stove. Set up a lightweight stove or propane\n" +
                        "stove. Light the stove, unless prohibited by local fire restrictions. Describe\n" +
                        "the safety procedures for using these types of stoves. ",
                " 2e) On one campout, plan and cook one hot breakfast or lunch, selecting\n" +
                        "foods from MyPlate or the current USDA nutritional model. Explain the\n" +
                        "importance of good nutrition. Demonstrate how to transport, store, and\n" +
                        "prepare the foods you selected. ",
                " 2f) Demonstrate tying the sheet bend knot. Describe a situation in which you\n" +
                        "would use this knot. ",
                " 2g) Demonstrate tying the bowline knot. Describe a situation in which you\n" +
                        "would use this knot. ",
                " 3a) Demonstrate how a compass works and how to orient a map. Use a map\n" +
                        "to point out and tell the meaning of five map symbols. ",
                " 3b) Using a compass and map together, take a 5-mile hike (or 10 miles by\n" +
                        "bike) approved by your adult leader and your parent or guardian.",
                " 3c) Describe some hazards or injuries that you might encounter on your hike\n" +
                        "and what you can do to help prevent them.",
                " 3d) Describe some hazards or injuries that you might encounter on your hike\n" +
                        "and what you can do to help prevent them.",
                " 4) Identify or show evidence of at least 10 kinds of wild animals (such\n" +
                        "as birds, mammals, reptiles, fish, or mollusks) found in your local area\n" +
                        "or camping location. You may show evidence by tracks, signs, or\n" +
                        "photographs you have taken. ",
                " 5a) Tell what precautions must be taken for a safe swim. ",
                " 5b) Demonstrate your ability to pass the BSA beginner test: Jump feetfirst into\n" +
                        "water over your head in depth, level off and swim 25 feet on the surface,\n" +
                        "stop, turn sharply, resume swimming, then return to your starting place.",
                " 5c) Demonstrate water rescue methods by reaching with your arm or leg, by\n" +
                        "reaching with a suitable object, and by throwing lines and objects.",
                " 5d) Explain why swimming rescues should not be attempted when a reaching\n" +
                        "or throwing rescue is possible. Explain why and how a rescue swimmer\n" +
                        "should avoid contact with the victim. ",
                " 6a) Demonstrate first aid for the following:\n" +
                        "• Object in the eye\n" +
                        "• Bite of a warm-blooded animal\n" +
                        "• Puncture wounds from a splinter, nail, and fishhook\n" +
                        "• Serious burns (partial thickness, or second-degree)\n" +
                        "• Heat exhaustion\n" +
                        "• Shock\n" +
                        "• Heatstroke, dehydration, hypothermia, and hyperventilation",
                " 6b) Show what to do for “hurry” cases of stopped breathing, stroke, severe\n" +
                        "bleeding, and ingested poisoning.",
                " 6c) Tell what you can do while on a campout or hike to prevent or reduce the\n" +
                        "occurrence of the injuries listed in Second Class requirements 6a and 6b",
                " 6d) Explain what to do in case of accidents that require emergency response\n" +
                        "in the home and backcountry. Explain what constitutes an emergency and\n" +
                        "what information you will need to provide to a responder. ",
                " 6e) Tell how you should respond if you come upon the scene of a\n" +
                        "vehicular accident.",
                " 7a) After completing Tenderfoot requirement 6c, be physically active at least\n" +
                        "30 minutes each day for five days a week for four weeks. Keep track of\n" +
                        "your activities. ",
                " 7b) Share your challenges and successes in completing Second Class\n" +
                        "requirement 7a. Set a goal for continuing to include physical activity as\n" +
                        "part of your daily life and develop a plan for doing so.",
                " 7c) Participate in a school, community, or troop program on the dangers\n" +
                        "of using drugs, alcohol, and tobacco and other practices that could be\n" +
                        "harmful to your health. Discuss your participation in the program with your\n" +
                        "family, and explain the dangers of substance addictions. Report to your\n" +
                        "Scoutmaster or other adult leader in your troop about which parts of the\n" +
                        "Scout Oath and Scout Law relate to what you learned. ",
                " 8a) Participate in a flag ceremony for your school, religious institution,\n" +
                        "chartered organization, community, or Scouting activity. ",
                " 8b) Explain what respect is due the flag of the United States. \n",
                " 8c) With your parents or guardian, decide on an amount of money that you\n" +
                        "would like to earn, based on the cost of a specific item you would like to\n" +
                        "purchase. Develop a written plan to earn the amount agreed upon and\n" +
                        "follow that plan; it is acceptable to make changes to your plan along the\n" +
                        "way. Discuss any changes made to your original plan and whether you\n" +
                        "met your goal. ",
                " 8d) At a minimum of three locations, compare the cost of the item for which you\n" +
                        "are saving to determine the best place to purchase it. After completing Second\n" +
                        "Class requirement 8c, decide if you will use the amount that you earned as\n" +
                        "originally intended, save all or part of it, or use it for another purpose. ",
                " 8e) Participate in two hours of service through one or more service projects\n" +
                        "approved by your Scoutmaster. Tell how your service to others relates to\n" +
                        "the Scout Oath.",
                " 9a) Explain the three R’s of personal safety and protection.",
                " 9b) Describe bullying; tell what the appropriate response is to someone who is\n" +
                        "bullying you or another person. ",
                " 10) Demonstrate Scout spirit by living the Scout Oath and Scout Law. Tell\n" +
                        "how you have done your duty to God and how you have lived four\n" +
                        "different points of the Scout Law (not to include those used for Tenderfoot\n" +
                        "requirement 9) in your everyday life. ",
                " 11) While working toward the Second Class rank, and after completing\n" +
                        "Tenderfoot requirement 10, participate in a Scoutmaster conference. ",
                " 12) Successfully complete your board of review for the Second Class rank."};

        String[] firstClass = {"Click here to see what you have Checked off already\n",
                " 1a) Since joining, participate in 10 separate troop/patrol activities, six of\n" +
                "which include overnight camping. These 10 activities do not include troop\n" +
                "or patrol meetings. On at least five of the six campouts, spend the night in\n" +
                "a tent that you pitch or other structure that you help erect (such as a lean-to,\n" +
                "snow cave, or tepee).",
                " 1b) Explain each of the principles of Tread Lightly! and tell how you practiced\n" +
                        "them on a campout or outing. This outing must be different from the ones\n" +
                        "used for Tenderfoot requirement 1c and Second Class requirement 1b.",
                " 2a) Help plan a menu for one of the above campouts that includes at least one\n" +
                        "breakfast, one lunch, and one dinner, and that requires cooking at least\n" +
                        "two of the meals. Tell how the menu includes the foods from MyPlate or the\n" +
                        "current USDA nutritional model and how it meets nutritional needs for the\n" +
                        "planned activity or campout.",
                " 2b) Using the menu planned in First Class requirement 2a, make a list showing\n" +
                        "a budget and the food amounts needed to feed three or more boys.\n" +
                        "Secure the ingredients.",
                " 2c) Show which pans, utensils, and other gear will be needed to cook and\n" +
                        "serve these meals.",
                " 2d) Demonstrate the procedures to follow in the safe handling and storage of\n" +
                        "fresh meats, dairy products, eggs, vegetables, and other perishable food\n" +
                        "products. Show how to properly dispose of camp garbage, cans, plastic\n" +
                        "containers, and other rubbish.",
                " 2e) On one campout, serve as cook. Supervise your assistant(s) in using a\n" +
                        "stove or building a cooking fire. Prepare the breakfast, lunch, and dinner\n" +
                        "planned in First Class requirement 2a. Supervise the cleanup.",
                " 3a) Discuss when you should and should not use lashings.",
                " 3b) Demonstrate tying the timber hitch and clove hitch.",
                " 3c) Demonstrate tying the square, shear, and diagonal lashings by joining two\n" +
                        "or more poles or staves together.",
                " 3d) Use lashings to make a useful camp gadget or structure.",
                " 4a) Using a map and compass, complete an orienteering course that covers\n" +
                        "at least one mile and requires measuring the height and/or width of\n" +
                        "designated items (tree, tower, canyon, ditch, etc.).",
                " 4b) Demonstrate how to use a handheld GPS unit, GPS app on a smartphone,\n" +
                        "or other electronic navigation system. Use GPS to find your current\n" +
                        "location, a destination of your choice, and the route you will take to get\n" +
                        "there. Follow that route to arrive at your destination.",
                " 5a) Identify or show evidence of at least 10 kinds of native plants found\n" +
                        "in your local area or campsite location. You may show evidence by\n" +
                        "identifying fallen leaves or fallen fruit that you find in the field, or as part of\n" +
                        "a collection you have made, or by photographs you have taken.",
                " 5b) Identify two ways to obtain a weather forecast for an upcoming activity.\n" +
                        "Explain why weather forecasts are important when planning for an event.",
                " 5c) Describe at least three natural indicators of impending hazardous weather,\n" +
                        "the potential dangerous events that might result from such weather conditions,\n" +
                        "and the appropriate actions to take.",
                " 5d) Describe extreme weather conditions you might encounter in the outdoors\n" +
                        "in your local geographic area. Discuss how you would determine ahead\n" +
                        "of time the potential risk of these types of weather dangers, alternative\n" +
                        "planning considerations to avoid such risks, and how you would prepare\n" +
                        "for and respond to those weather conditions. ",
                " 6a) Successfully complete the BSA swimmer test.",
                " 6b) Tell what precautions must be taken for a safe trip afloat.",
                " 6c) Identify the basic parts of a canoe, kayak, or other boat. Identify the parts\n" +
                        "of a paddle or an oar. ",
                " 6d) Describe proper body positioning in a watercraft, depending on the type\n" +
                        "and size of the vessel. Explain the importance of proper body position in\n" +
                        "the boat.",
                " 6e) With a helper and a practice victim, show a line rescue both as tender\n" +
                        "and as rescuer. (The practice victim should be approximately 30 feet from\n" +
                        "shore in deep water.)\n",
                " 7a) Demonstrate bandages for a sprained ankle and for injuries on the head,\n" +
                        "the upper arm, and the collarbone.",
                " 7b) By yourself and with a partner, show how to:\n" +
                        "• Transport a person from a smoke-filled room.\n" +
                        "• Transport for at least 25 yards a person with a sprained ankle",
                " 7c) Tell the five most common signals of a heart attack. Explain the steps\n" +
                        "(procedures) in cardiopulmonary resuscitation (CPR).",
                " 7d) Tell what utility services exist in your home or meeting place. Describe\n" +
                        "potential hazards associated with these utilities and tell how to respond in\n" +
                        "emergency situations.",
                " 7e) Develop an emergency action plan for your home that includes what to do\n" +
                        "in case of fire, storm, power outage, and water outage.",
                " 7f) Explain how to obtain potable water in an emergency.",
                " 8a) After completing Second Class requirement 7a, be physically active at\n" +
                        "least 30 minutes each day for five days a week for four weeks. Keep track\n" +
                        "of your activities. ",
                " 8b) Share your challenges and successes in completing First Class requirement\n" +
                        "8a. Set a goal for continuing to include physical activity as part of your\n" +
                        "daily life. ",
                " 9a) Visit and discuss with a selected individual approved by your leader (for\n" +
                        "example, an elected official, judge, attorney, civil servant, principal, or\n" +
                        "teacher) the constitutional rights and obligations of a U.S. citizen. ",
                " 9b) Investigate an environmental issue affecting your community. Share what\n" +
                        "you learned about that issue with your patrol or troop. Tell what, if anything,\n" +
                        "could be done by you or your community to address the concern.",
                " 9c) On a Scouting or family outing, take note of the trash and garbage you\n" +
                        "produce. Before your next similar outing, decide how you can reduce,\n" +
                        "recycle, or repurpose what you take on that outing, and then put those\n" +
                        "plans into action. Compare your results. ",
                " 9d) Participate in three hours of service through one or more service projects\n" +
                        "approved by your Scoutmaster. The project(s) must not be the same service\n" +
                        "project(s) used for Tenderfoot requirement 7b and Second Class requirement\n" +
                        "8e. Explain how your service to others relates to the Scout Law. ",
                " 10) Tell someone who is eligible to join Boy Scouts, or an inactive Boy Scout,\n" +
                        "about your Scouting activities. Invite him to an outing, activity, service\n" +
                        "project, or meeting. Tell him how to join, or encourage the inactive Boy\n" +
                        "Scout to become active. Share your efforts with your Scoutmaster or other\n" +
                        "adult leader",
                " 11) Demonstrate Scout spirit by living the Scout Oath and Scout Law. Tell how\n" +
                        "you have done your duty to God and how you have lived four different\n" +
                        "points of the Scout Law (different from those points used for previous ranks)\n" +
                        "in your everyday life.",
                " 12) While working toward the First Class rank, and after completing Second\n" +
                        "Class requirement 11, participate in a Scoutmaster conference.",
                " 13) Successfully complete your board of review for the First Class rank."};

        String[] star = {"Click here to see what you have Checked off already\n",
                " 1) Be active in your troop for at least four months as a First Class Scout.",
                " 2) As a First Class Scout, demonstrate Scout spirit by living the Scout Oath\n" +
                        "and Scout Law. Tell how you have done your duty to God and how you\n" +
                        "have lived the Scout Oath and Scout Law in your everyday life.",
                " 3) Earn six merit badges, including any four from the required list for Eagle.\n" +
                        "You may choose any of the 17 merit badges on the required list for Eagle\n" +
                        "to fulfill this requirement. See Eagle rank requirement 3 for this list. ",
                " 4) While a First Class Scout, participate in six hours of service through one or\n" +
                        "more service projects approved by your Scoutmaster. ",
                " 5) While a First Class Scout, serve actively in your troop for four months in\n" +
                        "one or more of the following positions of responsibility (or carry out a\n" +
                        "Scoutmaster-approved leadership project to help the troop):\n" +
                        "Boy Scout troop. Patrol leader, assistant senior patrol leader, senior\n" +
                        "patrol leader, troop guide, Order of the Arrow troop representative, den\n" +
                        "chief, scribe, librarian, historian, quartermaster, bugler, junior assistant\n" +
                        "Scoutmaster, chaplain aide, instructor, webmaster, or outdoor ethics guide.4\n" +
                        "Varsity Scout team. Captain, cocaptain, program manager, squad leader,\n" +
                        "team secretary, Order of the Arrow team representative, librarian, historian,\n" +
                        "quartermaster, chaplain aide, instructor, den chief, webmaster, or outdoor\n" +
                        "ethics guide.\n" +
                        "Venturing crew/Sea Scout ship. President, vice president, secretary,\n" +
                        "treasurer, den chief, quartermaster, historian, guide, boatswain,\n" +
                        "boatswain’s mate, yeoman, purser, storekeeper, or webmaster.\n" +
                        "Lone Scout. Leadership responsibility in your school, religious organization,\n" +
                        "club, or elsewhere in your community.",
                " 6) With your parent or guardian, complete the exercises in the pamphlet How\n" +
                        "to Protect Your Children From Child Abuse: A Parent’s Guide and earn the\n" +
                        "Cyber Chip award for your grade.",
                " 7) While a First Class Scout, participate in a Scoutmaster conference",
                " 8) Successfully complete your board of review for the Star rank."};

        String[] life = {"Click here to see what you have Checked off already\n",
                " 1) Be active in your troop for at least six months as a Star Scout.",
                " 2) As a Star Scout, demonstrate Scout spirit by living the Scout Oath and Scout\n" +
                        "Law. Tell how you have done your duty to God and how you have lived the\n" +
                        "Scout Oath and Scout Law in your everyday life.",
                " 3) Earn five more merit badges (so that you have 11 in all), including any three\n" +
                        "additional badges from the required list for Eagle. You may choose any of\n" +
                        "the 17 merit badges on the required list for Eagle to fulfill this requirement.\n" +
                        "See Eagle rank requirement 3 for this list. ",
                " 4) While a Star Scout, participate in six hours of service through one or more\n" +
                        "service projects approved by your Scoutmaster. At least three hours of this\n" +
                        "service must be conservation-related. ",
                " 5) While a Star Scout, serve actively in your troop for six months in one or more of\n" +
                        "the following troop positions of responsibility (or carry out a Scoutmaster-approved\n" +
                        "leadership project to help the troop).\n" +
                        "Boy Scout troop. Patrol leader, assistant senior patrol leader, senior patrol leader,\n" +
                        "troop guide, Order of the Arrow troop representative, den chief, scribe, librarian,\n" +
                        "historian, quartermaster, bugler, junior assistant Scoutmaster, chaplain aide,\n" +
                        "instructor, webmaster, or outdoor ethics guide.\n" +
                        "Varsity Scout team. Captain, cocaptain, program manager, squad leader, team\n" +
                        "secretary, Order of the Arrow team representative, librarian, historian, quartermaster,\n" +
                        "chaplain aide, instructor, den chief, webmaster, or outdoor ethics guide.\n" +
                        "Venturing crew/Sea Scout ship. President, vice president, secretary, treasurer,\n" +
                        "den chief, quartermaster, historian, guide, boatswain, boatswain’s mate, yeoman,\n" +
                        "purser, storekeeper, or webmaster.\n" +
                        "Lone Scout. Leadership responsibility in your school, religious organization, club,\n" +
                        "or elsewhere in your community.",
                " 6) While a Star Scout, use the Teaching EDGE method to teach another Scout\n" +
                        "(preferably younger than you) the skills from ONE of the following choices, so\n" +
                        "that he is prepared to pass those requirements to his Scoutmaster’s satisfaction.\n" +
                        "a. Tenderfoot 4a and 4b (first aid) e. First Class 4a and 4b (navigation)\n" +
                        "b. Second Class 2b, 2c, and f. Second Class 6a and 6b (first aid)\n" +
                        "2d (cooking/tools) g. First Class 7a and 7b (first aid)\n" +
                        "c. Second Class 3a and 3d (navigation)\n" +
                        "d. First Class 3a, 3b, 3c, and 3d (tools)\n" +
                        "e. First Class 4a and 4b (navigation)\n" +
                        "f. Second Class 6a and 6b (first aid)\n" +
                        "g. First Class 7a and 7b (first aid)\n" +
                        "h. Three requirements from one of\n" +
                        "the required Eagle merit badges,\n" +
                        "as approved by your Scoutmaster",
                " 7) While a Star Scout, participate in a Scoutmaster conference. ",
                " 8) Successfully complete your board of review for the Life rank."};

        String[] eagle = {"Click here to see what you have Checked off already\n",
                " 1) Be active in your troop for at least six months as a Life Scout.",
                " 2) As a Life Scout, demonstrate Scout Spirit by living the Scout Oath and Scout\n" +
                        "Law. Tell how you have done your duty to God, how you have lived the Scout\n" +
                        "Oath and Scout Law in your everyday life, and how your understanding\n" +
                        "of the Scout Oath and Scout Law will guide your life in the future. List on\n" +
                        "your Eagle Scout Rank Application the names of individuals who know\n" +
                        "you personally and would be willing to provide a recommendation on\n" +
                        "your behalf, including parents/guardians, religious (if not affiliated with an\n" +
                        "organized religion, then the parent or guardian provides this reference),\n" +
                        "educational, employer (if employed), and two other references.",
                " 3) Earn a total of 21 merit badges (10 more than required for the Life rank),\n" +
                        "including these 13 merit badges: (a) First Aid, (b) Citizenship in the Community,\n" +
                        "(c) Citizenship in the Nation, (d) Citizenship in the World, (e) Communication,\n" +
                        "(f) Cooking, (g) Personal Fitness, (h) Emergency Preparedness OR Lifesaving,\n" +
                        "(i) Environmental Science OR Sustainability, (j) Personal Management,\n" +
                        "(k) Swimming OR Hiking OR Cycling, (l) Camping, and (m) Family Life.\n" +
                        "You must choose only one of the merit badges listed in categories h, i, and k.\n" +
                        "Any additional merit badge(s) earned in those categories may be counted as\n" +
                        "one of your eight optional merit badges used to make your total of 21.",
                " 4) While a Life Scout, serve actively in your troop for six months in one or\n" +
                        "more of the following positions of responsibility:\n" +
                        "Boy Scout troop. Patrol leader, assistant senior patrol leader, senior patrol\n" +
                        "leader, troop guide, Order of the Arrow troop representative, den chief,\n" +
                        "scribe, librarian, historian, quartermaster, junior assistant Scoutmaster,\n" +
                        "chaplain aide, instructor, webmaster, or outdoor ethics guide.\n" +
                        "Varsity Scout team. Captain, cocaptain, program manager, squad leader,\n" +
                        "team secretary, Order of the Arrow team representative, librarian, historian,\n" +
                        "quartermaster, chaplain aide, instructor, den chief, webmaster, or outdoor\n" +
                        "ethics guide.\n" +
                        "Venturing crew/Sea Scout ship. President, vice president, secretary,\n" +
                        "treasurer, quartermaster, historian, den chief, guide, boatswain,\n" +
                        "boatswain’s mate, yeoman, purser, storekeeper, or webmaster.\n" +
                        "Lone Scout. Leadership responsibility in your school, religious organization,\n" +
                        "club, or elsewhere in your community.",
                " 5) While a Life Scout, plan, develop, and give leadership to others in a service\n" +
                        "project helpful to any religious institution, any school, or your community. (The\n" +
                        "project must benefit an organization other than the Boy Scouts of America.)\n" +
                        "A project proposal must be approved by the organization benefiting from\n" +
                        "the effort, your Scoutmaster and unit committee, and the council or district\n" +
                        "before you start. You must use the Eagle Scout Service Project Workbook,\n" +
                        "BSA publication No. 512-927, in meeting this requirement. (To learn more\n" +
                        "about the Eagle Scout service project, see the Guide to Advancement, topics\n" +
                        "9.0.2.0 through 9.0.2.16.) ",
                " 6) While a Life Scout, participate in a Scoutmaster conference",
                " Import Reminder:  In preparation for your board of review, prepare and attach to your Eagle Scout Rank Application\n" +
                        "a statement of your ambitions and life purpose and a listing of positions held in your religious\n" +
                        "institution, school, camp, community, or other organizations, during which you demonstrated\n" +
                        "leadership skills. Include honors and awards received during this service.",
                " 7) Successfully complete your board of review for the Eagle Scout rank.10\n" +
                        "(This requirement may be met after age 18, in accordance with Guide to\n" +
                        "Advancement topic 8.0.3.1.11). "};

        regs = new LinkedHashMap<String, List<String>>();

        for (String ranks : groupList) {
            if (ranks.equals("Scout")) {
                loadChild(scout);
            } else if (ranks.equals("Tenderfoot"))
                loadChild(tenderfoot);
            else if (ranks.equals("Second Class"))
                loadChild(secondClass);
            else if (ranks.equals("First Class"))
                loadChild(firstClass);
            else if (ranks.equals("Star"))
                loadChild(star);
            else if (ranks.equals("Life"))
                loadChild(life);
            else
                loadChild(eagle);

            regs.put(ranks, childList);

            ScoutNum = scout.length;
            TenderfoorNum = tenderfoot.length;
            SecondClassNum = secondClass.length;
            FirstClassNum = firstClass.length;
            StarNum = star.length;
            LifeNum = life.length;
            EagleNum = eagle.length;
        }

    }

    private void loadChild(String[] regs) {
        childList = new ArrayList<String>();
        for (String lister : regs)
            childList.add(lister);
    }

    private int getRegNum(int groupPosition) {
        switch (groupPosition) {
            case 0:
                return ScoutNum;

            case 1:
                return TenderfoorNum;

            case 2:
                return SecondClassNum;

            case 3:
                return FirstClassNum;

            case 4:
                return StarNum;

            case 5:
                return LifeNum;

            case 6:
                return EagleNum;

            default: return -1;

        }

    }
    public void setupBool(){
        scoutTrac = new boolean[ScoutNum];
        tenderfootTrac = new boolean[TenderfoorNum];
        secondClassTrac = new boolean[SecondClassNum];
        firstClassTrac = new boolean[FirstClassNum];
        starTrac = new boolean[StarNum];
        lifeTrac = new boolean[LifeNum];
        eagleTrac = new boolean[EagleNum];
    }
    private void fillBool(int rank ,int reg, boolean done)
    {
        switch(rank){
            case 0: scoutTrac[reg]= done;
                break;
            case 1: tenderfootTrac [reg] = done;
                break;
            case 2: secondClassTrac[reg] = done;
                break;
            case 3: firstClassTrac[reg] = done;
                break;
            case 4: starTrac[reg] = done;
                break;
            case 5: lifeTrac[reg] = done;
                break;
            case 6: eagleTrac[reg] = done;
                break;
        }
    }

    public boolean getBool(int rank, int reg){
        switch(rank){
            case 0:  return scoutTrac[reg];

            case 1: return tenderfootTrac [reg];

            case 2: return secondClassTrac[reg];

            case 3: return firstClassTrac[reg];

            case 4: return starTrac[reg];

            case 5: return lifeTrac[reg];

            case 6: return eagleTrac[reg];

            default: return false;
        }

    }
    private void createDB(){

        for(int i = 0; i < FirstClassNum;i++)// creating table and filling values for first class
        {
            db.insertReg(3,firstClassTrac[i]);
        }

        for(int i = 0; i < ScoutNum;i++)// filling the values on the table
        {
            db.updateReg(0,scoutTrac[i],i);
        }

        for(int i = 0; i < TenderfoorNum;i++)
        {
            db.updateReg(1,tenderfootTrac[i],i);
        }

        for(int i = 0; i < SecondClassNum;i++)
        {
            db.updateReg(2,secondClassTrac[i],i);
        }
         for(int i = 0; i < StarNum;i++)
        {
            db.updateReg(4,starTrac[i],i);
        }

        for(int i = 0; i < LifeNum;i++)
        {
            db.updateReg(5,lifeTrac[i],i);
        }

        for(int i = 0; i < EagleNum;i++)
        {
            int j = i;

            db.updateReg(6,eagleTrac[i],j);
        }
    }

    private void updateBool(){
        int temp = 0;

        for(int i = 0; i < ScoutNum;i++)// filling the values on the table
        {
           Cursor sc = db.getData(0,i);
           sc.moveToFirst();
           temp = sc.getInt(sc.getColumnIndex("scout"));
            if(i < ScoutNum - 1) {
                int j = i;
                j++;
                scoutTrac[j] = isItTrue(temp);
            }

        }

        for(int i = 0; i < TenderfoorNum;i++)
        {
            Cursor tf = db.getData(1,i);
            tf.moveToFirst();
            temp = tf.getInt(tf.getColumnIndex("tenderfoot"));
            if(i < TenderfoorNum - 1) {
                int j = i;
                j++;
                tenderfootTrac[j] = isItTrue(temp);
            }

        }

        for(int i = 0; i < SecondClassNum;i++)
        {
            Cursor se = db.getData(2,i);
            se.moveToFirst();
            temp = se.getInt(se.getColumnIndex("secondClass"));
            if(i < SecondClassNum - 1 ) {
                int j = i;
                j++;
                secondClassTrac[j] = isItTrue(temp);
            }

        }
        for(int i = 0; i < 38;i++)// creating table and filling values for first class
        {
            Cursor fc = db.getData(3,i);
            fc.moveToFirst();
            temp = fc.getInt(fc.getColumnIndex("firstClass"));
            if(i < 38-1) {
                int j = i;
                j++;
                firstClassTrac[j] = isItTrue(temp);
            }

        }
        for(int i = 0; i < StarNum;i++)
        {
            Cursor st = db.getData(4,i);
            st.moveToFirst();
            temp = st.getInt(st.getColumnIndex("star"));
            if(i < StarNum - 1) {
                int j = i;
                j++;
                starTrac[j] = isItTrue(temp);
            }

        }

        for(int i = 0; i < LifeNum;i++)
        {
            Cursor li = db.getData(5,i);
            li.moveToFirst();
            temp = li.getInt(li.getColumnIndex("life"));
            if(i < LifeNum -1){
                int j = i;
                j++;
                lifeTrac[j] = isItTrue(temp);
            }

        }

        for(int i = 0; i < EagleNum;i++)
        {
            Cursor ea = db.getData(6,i);
                ea.moveToFirst();
            temp = ea.getInt(ea.getColumnIndex("eagle"));
            if(i < EagleNum-1) {
                int j = i;
                j++;
                eagleTrac[j] = isItTrue(temp);
            }

        }
    }

    private boolean isItTrue(int id){
        if(id == 1)
        {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean check(int id) {
        int counter = 0;
        switch(id) {
            case 0: for(int i=1; i<ScoutNum;i++)
                    {
                        if(scoutTrac[i]==true)
                        {
                            counter++;
                        }
                    }
                    if(counter == ScoutNum-1)
                    {
                        return true;
                    }
                    else
                    {
                        return true;
                    }
            default: return false;
        }

    }
}
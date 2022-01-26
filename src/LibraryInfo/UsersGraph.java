/* ------------------------------------------------------------------------- *
    * M1 2IS AP - Digital library assignment
    ------------------------------------------------------------------------
    * authors: Tasnim Safadi
    *          Omotolani Subair
    * date: 09/01/2022
    ------------------------------------------------------------------------
    ------------------------------------------------------------------------
    * In this file you will find the implementation of the UserGraph class.
    * This class is used to handle the connection graph.
    ------------------------------------------------------------------------
 */
package LibraryInfo;

import Medias.Media;
import Persons.BorrowDetails;
import Persons.Subscriber;

import java.util.*;

/* ------------------------------------------------------------------------- *
 * UsersGraph class
 * Attributes:
 *    usersGraph: the single instance of UsersGraph
 *    vertices: vertices of the graph (subscribers in this case)
 *    adjacency: adjacency of the graph (subscribers are connected
 *               if they have at least one media borrowed in common)
 */
public class UsersGraph{
    private static UsersGraph usersGraph = null;
    private static List<Subscriber> vertices;
    private static Map<Subscriber, List<Subscriber>> adjacency;

    /* ------------------------------------------------------------------------- *
     * Constructor of the class UserGraph which is private as only one instance of the
     * class can be created.
     *
     * RETURN
     * userGraph       A new instance of the class
     *
     */
    private UsersGraph() {
        // getting all the subscribers loaded
        vertices = Library.getSubscribers();
        adjacency = new HashMap<>();
        int l = vertices.size();
        ArrayList<Subscriber> unvisitedSubs = new ArrayList<>(vertices);
        // looping the all the subscribers
        for(int i = 0; i < l; i++) {
            // adding the subscriber in the adjacency map
            addSub(vertices.get(i));
            // removing the current subscriber from the unvisted
            // as we add in parallel this avoids looping over subscribers already added
            unvisitedSubs.remove(0);
            // going over the unvisited
            for(Subscriber s : unvisitedSubs) {
                Map<BorrowDetails, Media> bList = vertices.get(i).getBorrowList();
                Map<BorrowDetails, Media> sbList = s.getBorrowList();
                // connecting subscribers that have a borrowed media in common
                for(Map.Entry<BorrowDetails, Media> entry: bList.entrySet()){
                    if(sbList.containsValue(entry.getValue())){
                        connect(vertices.get(i), s);
                    }
                }
            }
        }
    }

    /* ------------------------------------------------------------------------- *
     * Getter of the UsersGraph class instance which instantiates the class on
     * the first call.
     *
     * RETURN
     * usersGraph       The single instance of the class UsersGraph
     *
     */
    public static UsersGraph getInstance()
    {
        if (usersGraph == null)
            usersGraph = new UsersGraph();

        return usersGraph;
    }

    /* ------------------------------------------------------------------------- *
     * This method adds a subscriber to the adjacency map.
     *
     * PARAMETER
     * s                The subscriber to be added
     *
     */
    private void addSub(Subscriber s){
        if(adjacency.containsKey(s))
            return;
        adjacency.put(s, new ArrayList());
    }

    /* ------------------------------------------------------------------------- *
     * This method returns a media as an inner recommendation for a subscriber
     * as described in the assignment.
     *
     * PARAMETER
     * s                The subscriber to recommend a media to
     *
     * RETURN
     * media            The media recommendation found
     *
     */
    public Media innerRec(Subscriber s){
        Media m;
        if (adjacency.containsKey(s)) {
            List<Subscriber> neighbours = adjacency.get(s);
            // getting a random index in the neighbours of s
            int l = neighbours.size();;
            int r = rand(0, l);
            // trying all the neighbours of s
            for(int i = 0; i < l; i++){
                m = getRecommendation(s, neighbours.get(r), new ArrayList<Media>());
                if(m != null)
                    return m;
                // going on the next neighbour if no recommendation was found
                r = (r + 1) % l;
            }
        }
        return null;
    }

    /* ------------------------------------------------------------------------- *
     * This method returns a media that is not borrowed by s1 but borrowed by s2
     * and isn't in the list "medias"
     *
     * PARAMETER
     * s1                The subscriber to recommend a media to
     * s2                The subscriber to look through the borrow
     * medias            The list of medias to not recommend
     *
     * RETURN
     * media            The media recommendation found
     *
     */
    private Media getRecommendation(Subscriber s1, Subscriber s2, ArrayList<Media> medias){
        Map<BorrowDetails, Media> s1BList = s1.getBorrowList();
        Map<BorrowDetails, Media> s2BList = s2.getBorrowList();
        // looping over the borrow list
        for(Map.Entry<BorrowDetails, Media> entry: s2BList.entrySet()) {
            // looking for a new recommendation
            if (!s1BList.containsValue(entry.getValue()) && ! medias.contains(entry.getValue())) {
                return entry.getValue();
            }
        }
        return null;
    }


    /* ------------------------------------------------------------------------- *
     * This method returns a random int in the interval [min, max[.
     *
     * PARAMETER
     * min               The lower bound of the interval
     * max               The higher bound of the interval
     *
     * RETURN
     * r                 The random int
     *
     */
    public int rand(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /* ------------------------------------------------------------------------- *
     * This method returns a random subscriber which is not in the neighbours of
     * s.
     *
     * PARAMETER
     * s                 The subscriber to not get neighbours from
     *
     * RETURN
     * randomSub         A random subscriber who is not the neighbour of s
     *
     */
    private Subscriber getRandomSub(Subscriber s){
        if(s == null || ! adjacency.containsKey(s))
            return null;
        int l = Library.getSubscribers().size();
        int r = rand(0, l);
        // getting a random subscriber from all subscribers
        Subscriber randomSub = Library.getSubscribers().get(r);
        int count = 0;
        // looping over all the subscribers until one isn't the neighbour of s or s
        while(s.getId() == randomSub.getId() || adjacency.get(s).contains(randomSub)) {
            r = (r + 1) % l;
            randomSub = Library.getSubscribers().get(r);
            count += 1;
            // if count == l, no random sub was found
            if(count == l)
                return null;
        }
        return randomSub;
    }

    /* ------------------------------------------------------------------------- *
     * This method connects two subscribers: s1 and s2.
     *
     * PARAMETER
     * s1                The subscriber to connect
     * s2                The subscriber to connect
     *
     */
    private void connect(Subscriber s1, Subscriber s2) {
        adjacency.get(s1).add(s2);
        if(adjacency.containsKey(s2))
            adjacency.get(s2).add(s1);
        else{
            addSub(s2);
            adjacency.get(s2).add(s1);
        }
    }

    @Override
    public String toString() {
        String msg="";
        for(Subscriber s: vertices){
            msg+="Subscriber: "+s+"\n";
            msg+="Neighbours:";
            for(Subscriber s2: adjacency.get(s)){
                msg+=" "+s2;
            }
            msg+="\n";
        }
        return msg;
    }

    /* ------------------------------------------------------------------------- *
     * This method returns a list of media as an discovery recommendation for a
     * subscriber as described in the assignment.
     *
     * PARAMETER
     * s                The subscriber to recommend medias to
     *
     * RETURN
     * medias           The list of media recommendation found
     *
     */
    public ArrayList<Media> discovery(Subscriber s1){
        ArrayList<Media> medias = new ArrayList<>();
        // setting destination subscriber
        Subscriber s2 = getRandomSub(s1);
        boolean[] discovered = new boolean[vertices.size()];
        // creating new stack which will be used for the path between s1 and s2
        Stack<Subscriber> path = new Stack<>();
        // creating the path if possible
        isReachable(s1, s2, discovered, path);
        int l = path.size();
        for(int i = 0; i < l ; i++){
            // getting recommendations through the path created
            Media m = getRecommendation(s1, path.get(i), medias);
            if(m != null)
                medias.add(m);
        }
        return medias;
    }

    /* ------------------------------------------------------------------------- *
     * This  recursive method returns true if a there is a path between src and
     * dst. It stops when all the subscribers have been discovered or when the
     * destination (dst) is reached
     *
     * PARAMETER
     * s                The subscriber to recommend medias to
     *
     * RETURN
     * medias           The list of media recommendation found
     *
     */
    private static boolean isReachable(Subscriber src, Subscriber dest,
                                      boolean[] discovered, Stack<Subscriber> path)
    {
        // mark the current subscriber as discovered
        discovered[src.getId()] = true;

        // include the current subscriber in the path
        path.add(src);

        // if destination subscriber is found
        if (src == dest) {
            return true;
        }

        // do for every edge (src, i)
        for (Subscriber s: adjacency.get(src))
        {
            // if s is not yet discovered
            if (!discovered[s.getId()])
            {
                // return true if the destination is found
                if (isReachable(s, dest, discovered, path)) {
                    return true;
                }
            }
        }

        // backtrack: remove the current subscriber from the path
        path.pop();

        // return false if destination subscriber is not reachable from src
        return false;
    }

    /*
    * We do not use this method but we could have used it in order to avoid re generating
    * the graph every time the user wants a recommendation but yet keeping it up to date
    * every time a new borrow is made

    public void updateGraph(Subscriber s, Media m){
        List<Subscriber> others = new ArrayList<>(vertices);
        others.remove(s);
        for(Subscriber o : others){
            if(o.getBorrowList().containsValue(m)){
                if(! adjacency.get(s).contains(o)){
                    adjacency.get(s).add(o);
                    adjacency.get(o).add(s);
                }
            }
        }
    }
    */

}


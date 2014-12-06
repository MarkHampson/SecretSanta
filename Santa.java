

public class Santa {

    private String name;
    private String email;
    private String[] exclude;
    private String wishList;
    private Santa giveTo;
    private Santa receiveFrom;

    public Santa(String s) {
        
        In in = new In(s);
        int i = s.lastIndexOf("/"); // if "/" doesn't exist, the whole
        name = s.substring(i + 1);  // string becomes the name
        email = in.readLine();  //first line is the email
        exclude = getExclusions(in.readLine());
        wishList = in.readAll(); //rest is the wish list
        giveTo = null;
        receiveFrom = null;
    }

    private String[] getExclusions(String s) {
        String[] ex;
        String parsed;

        if(s.matches("exclude:(.*)")) {
            int i = s.lastIndexOf(":");
            parsed = s.substring(i + 1); //remove the "exclude" keyword
            parsed = parsed.trim();
            ex = parsed.split(" ");
        }
        else {
            ex = null;
        }
        return ex;
    }

    public boolean isConflicted() {
        for(String str: exclude) {
            if(str.equals(giveTo.getName())) return true;
            if(giveTo.getName().equals(receiveFrom.getName())) return true;
            if(giveTo == receiveFrom) return true;
        }
        return false;
    }

    public void setGiveTo(Santa s) {
        giveTo = s;
    }

    public void setReceiveFrom(Santa s) {
        receiveFrom = s;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getWishList() {
        return wishList;
    }

    public String getMessage() {
        StringBuilder s = new StringBuilder();
        s.append("Ho ho ho. It's another secret santa.\n\n");
        s.append("You selected " + giveTo.getName() + ". ");
        s.append("Here is " + giveTo.getName() + "'s wishlist: \n\n");
        s.append(giveTo.getWishList());
        s.append("\n");
        s.append("Merry Christmas.");
        return s.toString();
    }
    public String toString() {
        return name + ": " + giveTo.getName(); 
    }
}

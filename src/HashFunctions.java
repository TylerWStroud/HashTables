public class HashFunctions {
  private static int[][] Table;
    private static final int[] keys = {
            1234, 8234, 7867, 1009, 5438, 4312, 3420, 9487, 5418, 5299,
            5078, 8239, 1208, 5098, 5195, 5329, 4543, 3344, 7698, 5412,
            5567, 5672, 7934, 1254, 6091, 8732, 3095, 1975, 3843, 5589,
            5439, 8907, 4097, 3096, 4310, 5298, 9156, 3895, 6673, 7871,
            5787, 9289, 4553, 7822, 8755, 3398, 6774, 8289, 7665, 5523};

    public static void main(String[] args){
        java.util.Scanner scan = new java.util.Scanner(System.in);

        // MAIN MENU
        boolean runMenu = true;
        while(runMenu){
            try{
                System.out.print("""
                        
                        -----MAIN MENU--------------------------------------
                        1. Run HF1 (Division method with Linear Probing)
                        2. Run HF2 (Division method with Quadratic Probing)
                        3. Run HF3 (Division method with Double Hashing)
                        4. Run HF4 (Custom Designed HF)
                        5. Exit program
                        
                        Enter option number:\s""");
                int input = Integer.parseInt(scan.nextLine());

                // in case user inputs an option not on the menu
                if(input < 1 || input > 5)
                    System.out.println("Invalid menu option. Please choose options 1 - 5.");

                Table = new int[50][2];
                switch(input){
                    /* HF1 */
                    case 1:
                        System.out.println("Testing method HF1()");
                        HF1();
                        print();
                        break;

                    /* HF2 */
                    case 2:
                        System.out.println("Testing method HF2()");
                        HF2();
                        print();
                        break;

                    /* HF3 */
                    case 3:
                        System.out.println("Testing method HF3()");
                        HF3();
                        print();
                        break;

                    /* HF4 */
                    case 4:
                        System.out.println("Testing method HF4()");
                        HF4();
                        print();
                        break;

                    /* Exiting */
                    case 5:
                        System.out.println("Exiting...");
                        runMenu = false;
                }
            }catch(NumberFormatException e){
                System.out.println("Invalid menu option. Please choose options 1-5");
            }
        }

    }

    /* Linear Probe w/ Div Method */
    public static void HF1(){
        int m = Table.length;
        for(int k: keys){
            int index =  k % m; // initial hash via division method
            int probeCount = 0;

            while(Table[index][0]!=0){
                index = (index+1) % m; // incrementing by one, reverting if index > Table.length (m)
                probeCount++;
            }

            Table[index][0] = k;
            Table[index][1] = probeCount;
        }
    }

    /* Quadratic Probe w/ Div Method */
    public static void HF2(){
        int m = Table.length;
        for(int k: keys){
            int index = k % m; // initial hash via division method
            int pow = 1;
            int probeCount = 0;

            while(Table[index][0] != 0){
                // key
                index = (k + pow*pow) % m; // adding key value to incremented power. Modulated by table length (m)
                pow++;
                probeCount++;
            }
            Table[index][0] = k;
            Table[index][1] = probeCount;
        }
    }

    /* Double hashing w/ Div Method */
    public static void HF3(){
        int m = Table.length;
        for(int k: keys){
            int index = k % m; // initial hash via division method
            int j = 1;
            int probeCount = 0;
            int h2 = 30-k%25; // second hash (30 minus key mod 25)

            while(Table[index][0] != 0){
                index = k % m + (j * h2); // double hashing

                probeCount++;
                if(index >= m){
                    index = index % m; // revert to start of table
                }
                j++;

                // cut off probing
                if(probeCount >= 50){
                    System.out.print("Unable to hash key "+ k +" to the table.\n");
                    probeCount = 0;
                    break;
                }
            }
            Table[index][0] = k;
            Table[index][1] = probeCount;
        }
    }

    /* Double Hashing w/ Folding Method */
    public static void HF4() {
        int m = Table.length;

        for (int k : keys) {
            // Convert key to a string to split into parts
            String keyStr = Integer.toString(k);
            int partSize = 2; // Split into 2-digit parts (adjustable)
            int sum = 0;

            // Folding: Split key into parts and sum them
            for (int i = 0; i < keyStr.length(); i += partSize) {
                int end = Math.min(i + partSize, keyStr.length());
                String part = keyStr.substring(i, end);
                sum += Integer.parseInt(part);
            }

            int index = sum % m; // initial hash via folding
            int j = 1;
            int probeCount = 0;
            int h2 = 1 + (k % (m - 1)); // secondary hash (relatively prime to m)

            // Double hashing for collision resolution
            while (Table[index][0] != 0) {
                index = (index + j * h2) % m;
                j++;
                probeCount++;

                if (probeCount >= 50) {
                    System.out.print("Unable to hash key " + k + " to the table.\n");
                    break;
                }
            }

            // cut off probing
            if (probeCount < 50) {
                Table[index][0] = k;
                Table[index][1] = probeCount;
            }
        }
    }

    // adds all probe values (Table[][1])
    public static int sumProbes(){
        int probes = 0;
        for(int[] col: Table){
            if(col[1] != 0){
                probes+= col[1];
            }
        }
        return probes;
    }

    // parsing for an empty table (no keys)
    public static boolean isEmpty(){
        for (int[] ints : Table) {
            if (ints[0] != 0)
                return false;
        }
        return true;
    }

    public static void print(){
        if(isEmpty())
            System.out.print("Table is empty.");

        else {
            System.out.println("Index\t Key\t Probes\n------------------------");
            for (int i = 0; i < Table.length; i++) {
                if (Table[i][0] != 0) {
                    System.out.println(i + "\t |\t" + Table[i][0] + "\t|\t" + Table[i][1]);
                }
            }
            System.out.println("------------------------\nSum of probe values = " + sumProbes());
        }
    }
}

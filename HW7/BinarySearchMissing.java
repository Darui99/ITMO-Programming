package search;

public class BinarySearchMissing {

    // Pre: s is not Empty
    public static int convert(String s) {
        try {
            return Integer.parseInt(s);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Inv: l <= r && arr[r] <= x && arr[l] > x
    public static int recBinSearch(int x, int l, int r, int[] arr) {
        if (r - l == 1) {
            // Pre: Inv && l + 1 == r (=>) r is answer
            if (r < arr.length && arr[r] == x)
                return r;
            else
                return -r - 1;
            // Post: answer was returned
        }

        // Pre: l <= r
        int mid = (l + r) / 2;
        // Post: l <= mid <= r

        if (arr[mid] <= x)
            // Pre: arr[mid] <= x && S && Inv (=>) for each i in [mid + 1; r] arr[i] <= x (=>) answer is in [l; mid]
            return recBinSearch(x, l, mid, arr);
            // Post: answer was returned
        else
            // Pre: arr[mid] > x && S && Inv (=>) for each i in [l; mid] arr[i] > x (=>) answer is in [mid; r]
            return recBinSearch(x, mid, r, arr);
        // Post: answer was returned
    }

    public static int binSearch(int x, int[] arr) {
        int l = -1, r = arr.length;

        // Inv: arr[l] > x && arr[r] <= x
        while (r - l > 1) {

            // Pre: l <= r
            int mid = (l + r) / 2;
            // Post: l <= mid <= r

            if (arr[mid] <= x)
                // Pre: arr[mid] <= x && S && Inv (=>) for each i in [mid + 1; r] arr[i] <= x (=>) answer is not righter than r
                r = mid;
                // Post: arr[r] <= x && Inv
            else
                // Pre: arr[mid] > x && S && Inv (=>) for each i in [l; mid] arr[i] > x (=>) answer is righter than l
                l = mid;
            // Post: arr[l] > x && Inv
        }
        // Post: T && S && Inv && l + 1 == r (=>) r is answer
        if (r < arr.length && arr[r] == x)
            return r;
        else
            return -r - 1;
    }

    public static void main(String[] args) {
        int x = convert(args[0]), n = args.length - 1;
        int[] arr = new int[args.length - 1];
        for (int i = 1; i < args.length; i++)
            arr[i - 1] = convert(args[i]);

        // Cond T: for each i in [0; n - 1] arr[i] and x have equal types
        // Cond S: for each i in [1; n - 1] arr[i] <= arr[i - 1]
        // Pre: T && S
        int resc = binSearch(x, arr);
        // Post: T && S && (1) resc >= 0 (=>) arr[resc] <= x && arr[resc - 1] > x 2) resc < 0 (=>) arr[-resc + 1] <= x && arr[-resc + 1] > x

        // Cond T: for each i in [0; n - 1] arr[i] and x have equal types
        // Cond S: for each i in [1; n - 1] arr[i] <= arr[i - 1]
        // Pre: T && S && r_f - l_f >= 1 && l_f in [-1; n] && r_f in [-1; n] && arr[l_f] > x && arr[r_f] <= x
        int resr = recBinSearch(x, -1, n, arr);
        // Post: T && S && (1) resr >= 0 (=>) arr[resr] <= x && arr[resr - 1] > x 2) resr < 0 (=>) arr[-resr + 1] <= x && arr[-resr + 1] > x

        if (resc != resr)
            System.exit(74);
        else
            System.out.print(resc);
    }
}

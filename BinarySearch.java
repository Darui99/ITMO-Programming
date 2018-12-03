package search;

public class BinarySearch {

    // Pre: s is not Empty
    public static int convert(String s) {
        try {
            return Integer.parseInt(s);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Inv: arr[r] <= x && arr[l] > x
    public static int recBinSearch(int x, int l, int r, int[] arr) {
        if (r - l == 1) {
            // Pre: Inv && l + 1 == r => r is answer
            return r;
        }

        // Pre: l <= r
        int mid = (l + r) / 2;
        // Post: l <= mid <= r

        if (arr[mid] <= x)
            // Pre: arr[mid] <= x && S (=>) for each i in [mid + 1; r] arr[i] <= x (=>) answer in [l; mid]
            return recBinSearch(x, l, mid, arr);
            // Post: answer was returned
        else
            // Pre: arr[mid] > x && S (=>) for each i in [l; mid] arr[i] > x (=>) answer in [mid; r]
            return recBinSearch(x, mid, r, arr);
            // Post: answer was returned
    }

    public static void main(String[] args) {
        int x = convert(args[0]), n = args.length - 1;
        int[] arr = new int[args.length - 1];
        for (int i = 1; i < args.length; i++)
            arr[i - 1] = convert(args[i]);

        int l = -1, r = n;

        // Cond T: for each i in [0; n - 1] arr[i] and x have equal types
        // Cond S: for each i in [1; n - 1] arr[i] <= arr[i - 1]
        // Pre: T && S
        // Inv: arr[l] > x && arr[r] <= x
        while (r - l > 1) {

            // Pre: l <= r
            int mid = (l + r) / 2;
            // Post: l <= mid <= r

            if (arr[mid] <= x)
                // Pre: arr[mid] <= x && S (=>) for each i in [mid + 1; r] arr[i] <= x
                r = mid;
                // Post: arr[r] <= x
            else
                // Pre: arr[mid] > x && S (=>) for each i in [l; mid] arr[i] > x
                l = mid;
                // Post: arr[l] > x
        }
        // Post: T && S && Inv && l + 1 == r (=>) r is answer

        int resc = r;

        // Cond T: for each i in [0; n - 1] arr[i] and x have equal types
        // Cond S: for each i in [1; n - 1] arr[i] <= arr[i - 1]
        // Pre: T && S
        int resr = recBinSearch(x, -1, n, arr);
        // Post: T && S && arr[resr] <= x && arr[resr - 1] > x

        if (resc != resr)
            System.out.print(-1);
        else
            System.out.print(resc);
    }
}

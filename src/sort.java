import java.util.Arrays;

public class sort {

	public static int j1, j2;
	
	public static void main(String[] args){
		int[] a = {2, 6, 1, 9, 5, 6, 7};
		randomized_quick_sort(a, 0, a.length - 1);
		System.out.print(Arrays.toString(a));
	}

	public static void partition3(int[] a, int l, int r) {
		int x = a[l];
		j1 = l;
		j2 = l;
		for (int i = l + 1; i <= r; i++) {
			if (a[i] <= x) {
				j1++;
				j2++;
				swap(a, j1, j2);
				swap(a, j1, i);
			} else if (a[i] == x) {
				j2++;
				swap(a, j2, i);
			}
		}
		swap(a, l, j1);
	}

	public static void randomized_quick_sort(int[] a, int l, int r) {
		if (l >= r) {
			return;
		}

		int k = l + (int) (Math.random() * (r - l + 1));
		swap(a, l, k);

		partition3(a, l, r);

		randomized_quick_sort(a, l, j1 - 1);
		randomized_quick_sort(a, j2 + 1, r);
	}
	
	public static void swap(int[] a, int i1, int i2){
		int temp = a[i1];
		a[i1] = a[i2];
		a[i2] = temp;
	}

}

import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of frames
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        // Input: Number of pages in the reference string
        System.out.print("Enter number of pages: ");
        int pagesCount = sc.nextInt();

        // Input: Page reference string
        System.out.print("Enter the page reference string: ");
        int[] pages = new int[pagesCount];
        for (int i = 0; i < pagesCount; i++) {
            pages[i] = sc.nextInt();
        }

        // Initialize frames and variables
        int[] frameArray = new int[frames];
        Arrays.fill(frameArray, -1); // -1 indicates an empty frame
        int pageFaults = 0;

        // Process each page in the reference string
        for (int i = 0; i < pagesCount; i++) {
            int currentPage = pages[i];
            boolean isPageInFrames = false;

            // Check if the page is already in frames
            for (int frame : frameArray) {
                if (frame == currentPage) {
                    isPageInFrames = true;
                    break;
                }
            }

            // If the page is not in frames (page fault)
            if (!isPageInFrames) {
                pageFaults++;

                // Find the optimal frame to replace
                int replaceIndex = -1;
                int farthest = i + 1;
                for (int j = 0; j < frames; j++) {
                    int k;
                    for (k = i + 1; k < pagesCount; k++) {
                        if (frameArray[j] == pages[k]) {
                            if (k > farthest) {
                                farthest = k;
                                replaceIndex = j;
                            }
                            break;
                        }
                    }
                    if (k == pagesCount) { // Frame's page is not used in future
                        replaceIndex = j;
                        break;
                    }
                }

                // Replace the frame or choose an empty frame
                if (replaceIndex == -1) {
                    replaceIndex = 0;
                }
                frameArray[replaceIndex] = currentPage;
            }

            // Print the current state of frames
            System.out.print("Frames after inserting page " + currentPage + ": ");
            for (int frame : frameArray) {
                if (frame == -1) {
                    System.out.print("[ ] ");
                } else {
                    System.out.print("[" + frame + "] ");
                }
            }
            System.out.println();
        }

        // Output the total page faults
        System.out.println("Total page faults: " + pageFaults);
        sc.close();
    }
}


import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get inputs for number of frames and pages
        System.out.println("Enter number of frames: ");
        int frames = sc.nextInt(); // Number of memory slots (frames)
        
        System.out.println("Enter number of pages: ");
        int pagesCount = sc.nextInt(); // Number of pages in the reference string

        // Input the page reference string
        int[] pages = new int[pagesCount];
        System.out.println("Enter the page reference string: ");
        for (int i = 0; i < pagesCount; i++) {
            pages[i] = sc.nextInt();
        }

        // Initialize frames array to keep track of pages in memory
        int[] frameArray = new int[frames];
        Arrays.fill(frameArray, -1); // Initialize all frames to -1 (empty)

        int pageFaults = 0; // Counter for page faults

        // Process each page in the reference string
        for (int i = 0; i < pagesCount; i++) {
            int currentPage = pages[i];

            // Check if the page is already in memory (no fault if found)
            if (!isInFrames(frameArray, currentPage)) {
                pageFaults++; // Increment page fault count

                // Find the best frame to replace using the optimal algorithm
                int replaceIndex = findOptimalIndex(frameArray, pages, i + 1);

                // Replace the chosen frame with the current page
                frameArray[replaceIndex] = currentPage;
            }

            // Print the current state of frames
            System.out.print("Frames after inserting page " + currentPage + ": ");
            printFrames(frameArray);
        }

        // Print the total number of page faults
        System.out.println("Total page faults: " + pageFaults);
        sc.close();
    }

    // Helper method: Check if a page is already in the frames
    private static boolean isInFrames(int[] frames, int page) {
        for (int frame : frames) {
            if (frame == page) {
                return true; // Page is found in memory
            }
        }
        return false; // Page not found
    }

    // Helper method: Find the optimal frame to replace
    private static int findOptimalIndex(int[] frames, int[] pages, int start) {
        int farthest = start; // Tracks the farthest future usage of a page
        int index = -1; // Index of the frame to replace

        for (int i = 0; i < frames.length; i++) {
            int j;
            // Search for the next occurrence of the current frame in the future
            for (j = start; j < pages.length; j++) {
                if (frames[i] == pages[j]) {
                    if (j > farthest) { // Update farthest if a later usage is found
                        farthest = j;
                        index = i;
                    }
                    break;
                }
            }

            // If the page in the frame is not found in the future, replace it
            if (j == pages.length) {
                return i;
            }
        }

        // If no frame has a future usage, return the first frame by default
        return (index == -1) ? 0 : index;
    }

    // Helper method: Print the current state of frames
    private static void printFrames(int[] frames) {
        for (int frame : frames) {
            if (frame == -1) {
                System.out.print("[ ] "); // Empty frame
            } else {
                System.out.print("[" + frame + "] "); // Frame with a page
            }
        }
        System.out.println();
    }
}


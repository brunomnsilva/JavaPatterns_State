package pt.pa.model;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author brunomnsilva
 */
public class Playlist implements Iterable<Song> {
    private List<Song> playlist;
    private int currentIndex;

    public Playlist() {
        playlist = new ArrayList<>();
        currentIndex = -1;
    }

    public void readFromFolder(String folderPath) throws IOException {
        File folderFile = new File(folderPath);
        File[] files = folderFile.listFiles(new WavFilenameFilter());
        for(File f : files) {
            playlist.add(new Song(f));
        }

        if(!isEmpty()) currentIndex = 0;
    }

    public boolean isEmpty() {
        return playlist.isEmpty();
    }

    public Song getCurrent() {
        if(isEmpty()) return null;

        return playlist.get(currentIndex);
    }

    public Song getNext() {
        if(isEmpty()) return null;

        currentIndex = (currentIndex + 1) % size();
        return playlist.get(currentIndex);
    }

    public Song getPrevious() {
        if(isEmpty()) return null;

        currentIndex = (size() + (currentIndex - 1)) % size();
        return playlist.get(currentIndex);
    }

    /**
     * Returns the size of the playlist.
     *
     * @return          returns the size of the playlist.
     */
    public int size() {
        return playlist.size();
    }

    @Override
    public Iterator<Song> iterator() {
        return playlist.iterator();
    }

    private class WavFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File file, String s) {
            return (s.endsWith(".wav"));
        }
    }
}

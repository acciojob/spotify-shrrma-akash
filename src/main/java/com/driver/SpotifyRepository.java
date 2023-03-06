package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User currentUser: users){
            if(currentUser.getMobile().equals(mobile)){
                return currentUser;
            }
        }
        User newUser= new User(name,mobile);
        users.add(newUser);
        return newUser;
    }

    public Artist createArtist(String name) {
        for(Artist currentArtist: artists){
            if(currentArtist.getName().equals(name))
                return currentArtist;
        }
        Artist newArtist = new Artist(name);
        artists.add(newArtist);
        return newArtist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist= createArtist(artistName);
        for(Album currentAlbum : albums){
            if(currentAlbum.getTitle().equals(title))
                return  currentAlbum;
        }
        //create new album
        Album newAlbum = new Album(title);
        //adding album to listDatabase
        albums.add(newAlbum);

        //putting artist and album in Database
        List<Album> album = new ArrayList<>();
        if(artistAlbumMap.containsKey(artist)){
            album=artistAlbumMap.get(artist);
        }
        album.add(newAlbum);
        artistAlbumMap.put(artist,album);
        return newAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean albumPresent = false;
        Album newAlbum = new Album();
        for(Album CurAlbum : albums){
            if(CurAlbum.getTitle().equals(albumName)){
                newAlbum=CurAlbum;
                albumPresent=true;
                break;
            }
        }
        if(albumPresent==false){
            throw new Exception("Album does not exist");
        }
        Song song = new Song(title,length);
        //adding song to list songs
        songs.add(song);

        //adding album n its song to albumsongsMap
        List<Song> listOfSongs= new ArrayList<>();
        if(albumSongMap.containsKey(newAlbum)){
            listOfSongs=albumSongMap.get(newAlbum);
        }
        listOfSongs.add(song);
        albumSongMap.put(newAlbum,listOfSongs);

        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlist);

        List<Song> tempList= new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                tempList.add(song);
            }
        }

        playlistSongMap.put(playlist,tempList);

        User currentUser= new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currentUser=user;
                flag= true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }


        List<User> usersList = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            usersList=playlistListenerMap.get(playlist);
        }
        usersList.add(currentUser);
        playlistListenerMap.put(playlist,usersList);


        creatorPlaylistMap.put(currentUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();

        if(userPlaylistMap.containsKey(currentUser)){
            userplaylists=userPlaylistMap.get(currentUser);
        }
        userplaylists.add(playlist);

        userPlaylistMap.put(currentUser,userplaylists);

        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist newPlaylist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(newPlaylist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                temp.add(song);
            }
        }
        playlistSongMap.put(newPlaylist,temp);

        User currentUser= new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currentUser=user;
                flag= true;
                break;
            }
        }

        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(newPlaylist)){
            userslist=playlistListenerMap.get(newPlaylist);
        }
        userslist.add(currentUser);

        playlistListenerMap.put(newPlaylist,userslist);

        creatorPlaylistMap.put(currentUser,newPlaylist);

        List<Playlist>userplaylists = new ArrayList<>();

        if(userPlaylistMap.containsKey(currentUser)){
            userplaylists=userPlaylistMap.get(currentUser);
        }
        userplaylists.add(newPlaylist);
        userPlaylistMap.put(currentUser,userplaylists);
        return newPlaylist;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag =false;
        Playlist newPlaylist = new Playlist();
        for(Playlist curplaylist: playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                newPlaylist=curplaylist;
                flag=true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("Playlist does not exist");
        }

        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }

//        public HashMap<Playlist, List<User>> playlistListenerMap;
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(newPlaylist)){
            userslist=playlistListenerMap.get(newPlaylist);
        }
        if(!userslist.contains(curUser))
            userslist.add(curUser);
        playlistListenerMap.put(newPlaylist,userslist);

//        public HashMap<User, Playlist> creatorPlaylistMap;
        if(creatorPlaylistMap.get(curUser)!=newPlaylist)
            creatorPlaylistMap.put(curUser,newPlaylist);

//        public HashMap<User, List<Playlist>> userPlaylistMap;
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }

        if(!userplaylists.contains(newPlaylist))userplaylists.add(newPlaylist);
        userPlaylistMap.put(curUser,userplaylists);


        return newPlaylist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User currentUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                currentUser=user;
                flag2= true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean flag = false;
        for(Song cursong : songs){
            if(cursong.getTitle().equals(songTitle)){
                song=cursong;
                flag=true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("Song does not exist");
        }

        //public HashMap<Song, List<User>> songLikeMap;
        List<User> users = new ArrayList<>();

        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }

        if (!users.contains(currentUser)){
            users.add(currentUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);


//            public HashMap<Album, List<Song>> albumSongMap;
            Album album = new Album();
            for(Album currentAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(currentAlbum);
                if(temp.contains(song)){
                    album=currentAlbum;
                    break;
                }
            }


//            public HashMap<Artist, List<Album>> artistAlbumMap;
            Artist artist = new Artist();
            for(Artist curArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist=curArtist;
                    break;
                }
            }
            artist.setLikes(artist.getLikes()+1);
        }
        return song;

    }

    public String mostPopularArtist() {
        String name="";
        int maximumLikes = Integer.MIN_VALUE;
        for(Artist artist : artists){
            maximumLikes= Math.max(maximumLikes,artist.getLikes());
        }
        for(Artist art : artists){
            if(maximumLikes==art.getLikes()){
                name=art.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name="";
        int maximumLikes = Integer.MIN_VALUE;
        for(Song song : songs){
            maximumLikes=Math.max(maximumLikes,song.getLikes());
        }
        for(Song song : songs){
            if(maximumLikes==song.getLikes())
                name=song.getTitle();
        }
        return name;
    }
}

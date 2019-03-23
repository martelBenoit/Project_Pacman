package main;

import main.view.GameFrame;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

class Maze {

    private String pathMaze;
    private int nbXTiles, nbYTiles;
    private int tile_size;

    private int powerTime;
    private int fruitValue;
    private int pillValue;
    private int regenerationTime;
    
    private Pacman pacman;

    private ArrayList<Tile> tiles;
    private ArrayList<Ghost> ghosts;
    private ArrayList<Pill> pills;

    Maze(int mazeNumber){

        this.pathMaze = "lib/"+mazeNumber+".maze";
        this.tiles = new ArrayList<>();
        this.pills = new ArrayList<>();
        this.ghosts = new ArrayList<>();
        this.createMaze();

        Tile pacmanSpawnTile = this.getPacmanSpawnTile();
        Tile ghostSpawnTile = this.getGhostSpawnTile();

        this.pacman = new Pacman(pacmanSpawnTile == null ? getRandomTile() : pacmanSpawnTile);
        for (int i = 0; i < 4 ; i++) {
            Ghost g = new Ghost(ghostSpawnTile == null ? getRandomTile() : ghostSpawnTile,i+1);
            this.ghosts.add(g);
        }
    }

    Tile getPacmanSpawnTile() {
        Tile pacmanSpawnTile = null;
        for(Tile t: tiles) {
            if (t.isPacmanSpawn()) {
                pacmanSpawnTile = t;
            }
        }
        return pacmanSpawnTile;
    }

    Tile getGhostSpawnTile() {
        Tile ghostSpawnTile = null;
        for(Tile t: tiles) {
            if (t.isGhostSpawn()) {
                ghostSpawnTile = t;
            }
        }
        return ghostSpawnTile;
    }

    private void createMaze(){
        try{
            // Ouverture du fichier pour la lecture
            InputStream ips=new FileInputStream(this.pathMaze);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);

            // Lecture de la dimension du plateau
            String line = br.readLine();
            String[] param = line.split(",");
            this.nbXTiles = Integer.parseInt(param[0]);
            this.nbYTiles = Integer.parseInt(param[1]);

            // Lecture des données propre au plateau
            line = br.readLine();
            param = line.split(",");
            this.pillValue = Integer.parseInt(param[0]);
            this.fruitValue = Integer.parseInt(param[1]);
            this.powerTime = Integer.parseInt(param[2]);
            this.regenerationTime = Integer.parseInt(param[3]);

            // On récupère la taille de la fenêtre pour adapter la taille des cases du tableau
            GameFrame gameFrame = GameFrame.getGameFrame();
            this.tile_size = (int)((GameFrame.WIDTH-GameFrame.HEIGHT)/1.15/this.nbXTiles);


            gameFrame.setDimensionPan(this.nbXTiles*this.tile_size,this.nbYTiles*this.tile_size);


            int i = 0;
            // On lit toute les lignes du fichier
            while ((line=br.readLine())!=null){

                int j = 0;                   // La colonne de la map
                int tmpx;               // Variable utilisée pour stocké la position en x d'une figure
                int tmpy;                // Variable utilisée pour stocké la position en y d'une figure

                param = line.split(",");

                for (String str : param) {

                    tmpx = j*this.tile_size;
                    tmpy = i*this.tile_size;
                    Tile newTile;
                    Pill newPill;
                    switch (str) {
                        case "P" : // Si c'est une PowerPill
                            newTile = new Tile(this.tile_size, tmpx, tmpy, false);
                            newPill = new PowerPill(newTile, Color.WHITE);
                            this.tiles.add(newTile);
                            this.pills.add(newPill);
                            break;
                        case "F" :  // Si c'est une case avec un fruit dessus
                            newTile = new Tile(this.tile_size, tmpx, tmpy, false);
                            newPill = new FruitPill(newTile,Color.RED);
                            this.tiles.add(newTile);
                            this.pills.add(newPill);
                            break;
                        case "E" :  // Si c'est une case avec rien
                            newTile = new Tile(this.tile_size, tmpx, tmpy, false);
                            this.tiles.add(newTile);
                            break;
                        case "S" :  // Si c'est le spawn de Pacman
                            newTile = new Tile(this.tile_size, tmpx, tmpy, false);
                            newTile.setPacmanSpawn();
                            this.tiles.add(newTile);
                            break;
                        case "G" :  // Si c'est le spawn des fantomes
                            newTile = new Tile(this.tile_size, tmpx, tmpy, false);
                            newTile.setGhostSpawn();
                            this.tiles.add(newTile);
                            break;
                        case "#" :  // Si c'est un mur
                            newTile = new Tile(this.tile_size, tmpx, tmpy, true);
                            this.tiles.add(newTile);
                            break;
                        case "_" :  // Si c'est une case avec une gomme dessus
                            newTile = new Tile(this.tile_size, tmpx, tmpy, false);
                            newPill = new NormalPill(newTile);
                            this.tiles.add(newTile);
                            this.pills.add(newPill);
                            break;

                    }
                    j++;
                }
                i++;

            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * Get a random tile which isn't a wall in the maze and doesn't contain a powerPill
     * @return A random tile, which isn't a wall and doesn't contain a powerPill
     */
    Tile getRandomTile() {
        ArrayList<Tile> tilesAvailable = new ArrayList<>();
        ArrayList<Pill> pills = new ArrayList<>();
        for(Pill p : this.pills) {
            if (p instanceof PowerPill || p instanceof FruitPill) {
               pills.add(p);
            }
        }

        for(Tile t : this.tiles) {
            if (!t.isWall()) {
                boolean valide = true;
                for (Pill p : pills) {
                    if (p.getTile() == t) {
                        valide = false;
                    }
                }
                if (valide) {
                    tilesAvailable.add(t);
                }
            }
        }

        Random rd = new Random();
        int x = rd.nextInt(tilesAvailable.size());
        return tilesAvailable.get(x);
    }


    int getFruitValue() {
        return fruitValue;
    }


    int getPillValue() {
        return pillValue;
    }


    int getRegenerationTime() {
        return regenerationTime;
    }


    int getPowerTime() {
        return powerTime;
    }

    ArrayList<Pill> getPills() {
        return pills;
    }

    ArrayList<Ghost> getGhosts() {
        return ghosts;
    }
    
    Pacman getPacman() {
		return this.pacman;
	}

    Tile getTile(Character c, Direction direction){
        Tile characterTile = c.getTile();
        switch(direction) {
            case UP:
                for(Tile t: tiles) {
                    if(characterTile.getY() != 0) {
                        if (t.getX() == characterTile.getX() && t.getY() == characterTile.getY() - this.tile_size) {
                            return t;
                        }
                    } else {
                        if (t.getX() == characterTile.getX() && t.getY() == (this.nbYTiles-1)*this.tile_size) {
                            return t;
                        }
                    }
                }
                break;
            case DOWN:
                for(Tile t: tiles) {
                    if(characterTile.getY() != this.tile_size*(this.nbYTiles-1)) {
                        if (t.getX() == characterTile.getX() && t.getY() == characterTile.getY() + this.tile_size) {
                            return t;
                        }
                    } else {
                        if (t.getX() == characterTile.getX() && t.getY() == 0) {
                            return t;
                        }
                    }
                }
                break;
            case LEFT:
                for(Tile t: tiles) {
                    if(characterTile.getX() != 0) {
                        if (t.getX() == characterTile.getX() - this.tile_size && t.getY() == characterTile.getY()) {
                            return t;
                        }
                    } else {
                        if (t.getX() == (this.nbXTiles-1)* this.tile_size && t.getY() == characterTile.getY()) {
                            return t;
                        }
                    }
                }
                break;
            case RIGHT:
                for(Tile t: tiles) {
                    if(characterTile.getX() != this.tile_size*(this.nbXTiles-1)) {
                        if (t.getX() == characterTile.getX() + this.tile_size && t.getY() == characterTile.getY()) {
                            return t;
                        }
                    } else {
                        if (t.getX() == 0 && t.getY() == characterTile.getY()) {
                            return t;
                        }
                    }
                }
                break;
            default:
                return null;
        }
        return null;
    }

    ArrayList<Tile> getTilesAround(Character c) {

        Tile characterTile = c.getTile();
        ArrayList<Tile> ret = new ArrayList<>();
        for(Tile t: tiles) {
            if(!t.isWall()) {
                if (t.getX() >= characterTile.getX() - this.tile_size && t.getX() <= characterTile.getX() + this.tile_size) {
                    if(t.getX() == characterTile.getX()) {
                        if((t.getY() >= characterTile.getY()-this.tile_size && t.getY() <= characterTile.getY()+this.tile_size) || t.getY() == characterTile.getY()-this.tile_size+((this.nbYTiles-1)*this.tile_size) || t.getY() == characterTile.getY()-((this.nbYTiles-1)*this.tile_size)) {
                            if(t.getY() != characterTile.getY()) {
                                ret.add(t);
                            }
                        }
                    }
                    else {
                        if(t.getY() == characterTile.getY()) {
                            ret.add(t);
                        }
                    }
                }
                else {
                     if (characterTile.getX() == 0) {
                         if(t.getX() == (this.nbXTiles-1)*this.tile_size) {
                             if(t.getY() == characterTile.getY()) {
                                 ret.add(t);
                             }
                         }
                     }
                     else if (characterTile.getX() == (this.nbXTiles-1)*this.tile_size) {
                         if(t.getX() == 0) {
                             if(t.getY() == characterTile.getY()) {
                                 ret.add(t);
                             }
                         }
                     }
                }
            }
        }
        return ret;
    }

    void draw () {

        for(Tile t: this.tiles) {
            t.draw();
        }

        for(Pill p: this.pills) {
            p.draw();
        }

        this.getPacman().draw();

        for(Ghost g: this.ghosts){
            g.draw();
        }

    }

}

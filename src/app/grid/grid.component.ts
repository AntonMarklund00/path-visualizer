// @ts-nocheck
import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  grid: Number[][] = [];
  gridWidth: number = 30;
  gridHeight: number = 20;
  solution: any;
  buttonDisabled: boolean = false;
  startPositionY: number = 10;
  startPositionX: number = 3;
  endPositionY: number = 10;
  endPositionX: number = 26;
  mouseDown: boolean = false
  isWallMove: boolean = true;
  moveStart: boolean = false;
  moveEnd: boolean = false;

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.cleanGrid();
  }

  cleanGrid() {
    this.grid = [];
    for (let i = 0; i < this.gridHeight; i++) {
      for (let j = 0; j < this.gridWidth; j++) {
        if (i == 0 || i == this.gridHeight - 1 || j == 0 || j == this.gridWidth - 1) {
          this.grid[i] = this.grid[i] || [];
          this.grid[i][j] = 1;
        } else {
          this.grid[i] = this.grid[i] || [];
          this.grid[i][j] = 0;
        }
      }
    }
    this.grid[this.startPositionY][this.startPositionX] = 2;
    this.grid[this.endPositionY][this.endPositionX] = 3;
  }

  onMouseDown(y: number, x: number) {
    this.makeWall(y, x);

  }

  onMouseOver(y: number, x: number) {
    if (this.mouseDown) {
      this.makeWall(y, x);
    }
  }

  onMouseUp() {
    this.mouseDown = false;
  }


  makeWall(y: number, x: number) {
    if (!this.mouseDown){
      this.isWallMove = this.grid[y][x] == 0;
      this.mouseDown = true
    }

    if (this.grid[y][x] == 2 && !this.moveEnd) {
      this.grid[y][x] = 0;
      this.moveStart = true;
    }

    if (this.grid[y][x] == 3 && !this.moveStart) {
      this.grid[y][x] = 0;
      this.moveEnd = true;
    }

    if (this.isWallMove) {
      if (this.moveStart){
        this.grid[y][x] = 2;
        this.moveStart = false;
        this.startPositionX = x;
        this.startPositionY = y;
      }else if(this.moveEnd){
        this.grid[y][x] = 3;
        this.moveEnd = false;
        this.endPositionX = x;
        this.endPositionY = y;
      } else{
        this.grid[y][x] = 1;
      }
    } else {
      this.grid[y][x] = 0;
    }
  }

  async solveGridWithAStar(){
    this.buttonDisabled = true;
    await this.getAStarSolutionFromApi();
    await this.drawVisitedSolution();
    this.drawSolution();
    this.buttonDisabled = false;
  }

  async solveGridWithDjikstras(){
    this.buttonDisabled = true;
    await this.getDjikstrasSolutionFromApi()
    await this.drawVisitedSolution();
    this.drawSolution();
    this.buttonDisabled = false;
  }

  async getAStarSolutionFromApi(){
    // @ts-ignore
    const data = await this.http.get('/api/astar',
        {params: {
          'grid': this.grid,
            'starty': this.startPositionY,
            'startx': this.startPositionX,
            'endy': this.endPositionY,
            'endx': this.endPositionX}})
        .toPromise()

    this.solution = data;
  }

  async getDjikstrasSolutionFromApi(){
    // @ts-ignore
    const data = await this.http.get('/api/djikstra',
        {params: {
          'grid': this.grid,
            'starty': this.startPositionY,
            'startx': this.startPositionX,
            'endy': this.endPositionY,
            'endx': this.endPositionX}})
        .toPromise()

    this.solution = data;
  }

  async drawVisitedSolution(){
    let visited = this.solution[0];

    for (let i = 0; i < visited.length; i++) {
      await new Promise(resolve => setTimeout(resolve, 15));
      this.grid[visited[i][0]][visited[i][1]] = 4;
    }
  }

  async drawSolution(){
    let solution = this.solution[1];
    for (let i = 0; i < solution.length; i++) {
      await new Promise(resolve => setTimeout(resolve, 15));
      this.grid[solution[i][0]][solution[i][1]]=5;
    }
  }

  timeout(ms){
    return new Promise(resolve => setTimeout(resolve, ms));
  }


  async delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  clearSolution(){
    this.clearVisitedPaths();
    this.clearSolutionPath();
  }

  clearVisitedPaths(){
    let visited = this.solution[0];
    for (let i = 0; i < visited.length; i++) {
      if (this.grid[visited[i][0]][visited[i][1]] != 1){
        this.grid[visited[i][0]][visited[i][1]] = 0;
      }
    }
  }

  clearSolutionPath(){
    let solution = this.solution[1];
    for (let i = 0; i < solution.length; i++) {
      if(this.grid[solution[i][0]][solution[i][1]] != 1){
        this.grid[solution[i][0]][solution[i][1]] = 0;
      }
    }
  }

  setStartAndEnd(event){
    this.startPositionY = event[0][0]
    this.startPositionX = event[0][1]

    this.endPositionY = event[1][0]
    this.endPositionX = event[1][1]
  }

}

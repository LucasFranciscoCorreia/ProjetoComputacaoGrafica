<h1 align="center" id="title">RenderCraft</h1>

<p align="center"><img src="https://socialify.git.ci/LucasFranciscoCorreia/RenderCraft/image?description=1&amp;forks=1&amp;issues=1&amp;language=1&amp;name=1&amp;owner=1&amp;pattern=Circuit+Board&amp;pulls=1&amp;stargazers=1&amp;theme=Auto" alt="project-image"></p>

<p id="description">This project is a graphical application that visualizes a 3D object in a frame of 800x800. This project is designed as a project for Basic Computer Graphics at UFRPE offered in 2018.1</p>
<H1 id="warning">WARNING</H1>
<p>Despite my efforts, this project is not optimal, nor does it use a graphics card to render the 3D object. It uses a single thread to render it. The existence of this project is for academic purposes, as much as it was for me as a project for Basic Computer Graphics if anyone ever desires to know how the rendering process works.</p>

<p align="center"><img src="https://img.shields.io/github/downloads/LucasFranciscoCorreia/RenderCraft/total" alt="shields"><img src="https://img.shields.io/github/issues/LucasFranciscoCorreia/RenderCraft" alt="shields"><img src="https://img.shields.io/github/issues-pr/LucasFranciscoCorreia/RenderCraft" alt="shields"><img src="https://img.shields.io/github/license/LucasFranciscoCorreia/RenderCraft" alt="shields"><img src="https://img.shields.io/github/repo-size/LucasFranciscoCorreia/RenderCraft" alt="shields"></p>

<h2>🛠️ Installation Steps:</h2>

<p>1. Download and Install JDK 23</p>

```
https://bell-sw.com/pages/downloads/#jdk-21-lts
```

<p>2. Download this repository</p>

```
git clone https://github.com/LucasFranciscoCorreia/RenderCraft.git
```

<p>3. Go to the project directory</p>

```
cd RenderCraft
```

<p>4. Run the demo</p>

```bash
java -jar RenderCraft.jar
```


<div align="center">

![rendercraft](https://github.com/LucasFranciscoCorreia/RenderCraft/blob/master/readme/rendercraftdemo.gif?raw=true)

</div>

<h1>Rendering Different Objects</h1>
<p>If you ever desire to render another object that's not a chalice, you can edit the `triangulos.txt` with the data of the object you like, as long as it's using the following template in a text file:</p>

```css
<number of points> <number of triangles>
<point1X> <point1Y> <point1z>
<point2X> <point2Y> <point2z>
<point3X> <point1Y> <point3z>
...
<pointNX> <pointNY> <pointNz>
<triangle1Point1> <triangle1Point2> <triangle1Point3>
<triangle2Point1> <triangle2Point2> <triangle2Point3>
<triangle3Point1> <triangle3Point2> <triangle3Point3>
...
<triangleMPoint1> <triangleMPoint2> <triangleMPoint3>
```

<h2>🛡️ License:</h2>

This project is licensed under the GNU General Public License v3.0
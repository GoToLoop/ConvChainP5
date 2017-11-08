/** 
 * ConvChainP5 (v0.2)
 * GoToLoop (2017/Nov/08)
 *
 * Forum.Processing.org/two/discussion/24889/
 * help-running-a-piece-of-code-convchainjava#Item_20
 *
 * https://GitHub.com/buckle2000/ConvChainJava
 * https://GitHub.com/mxgmn/ConvChain
 */

import ConvChain.Generator;

static final color BLACK = PImage.ALPHA_MASK, WHITE = -1;
static final int GRID = 32;

Generator generator;
PImage image, output, bg;

void setup() {
  size(256, 128);
  noSmooth();
  noLoop();
  frameRate(60.0);
  colorMode(RGB, 1);

  image = loadImage("Less Rooms.bmp");
  generator = new Generator(toArray(image), 3, 1.2, GRID, GRID);
  updateOutput();

  bg = image.get();
  bg.resize(width>>1, height);
  set(0, 0, bg);
}

void draw() {
  image(output, width>>1, 0, width>>1, height);
}

void keyPressed() {
  generator.iterate();
  updateOutput();
  redraw = true;
}

void mousePressed() {
  keyPressed();
}

void updateOutput() {
  output = toPImage(generator.fields, output);
}

static final boolean[][] toArray(final PImage img) {
  img.loadPixels();

  final color[] p = img.pixels;
  final int w = img.width, h = img.height;
  final boolean[][] arr = new boolean[h][w];

  for (int y = 0; y < h; ++y) {
    final boolean[] row = arr[y];
    final int r = y*w;
    for (int x = 0; x < w; row[x] = p[r + x++] == WHITE);
  }

  return arr;
}

PImage toPImage(final boolean[][] arr, PImage img) {
  final int rows = arr.length, cols = arr[0].length;

  if (img == null || img.width != cols || img.height != rows)
    img = createImage(cols, rows, RGB);

  final color[] p = img.pixels;
  final int w = img.width;

  for (int y = 0; y < rows; ++y) {
    final boolean[] row = arr[y];
    final int r = y*w;
    for (int x = 0; x < cols; p[r + x] = row[x++]? WHITE : BLACK);
  }

  img.updatePixels();
  return img;
}

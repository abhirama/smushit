This is a command line tool which losslessly compresses all the images in the passed directory using yahoo!'s smush.it image compression utility - http://www.smushit.com/ysmush.it/

Directions for use:
java -jar smushit.jar [options]

Mandatory options:
-imageDir                  
Root directory of the images to be smushed. The program recursively traverses all sub directories of this directory smushing images.

Optional Options:
-verbose<true|false> 
Will out put details messages on screen.
-dryRun<true|false>
Will not download the smushed images.
-imgExtensions
Specify a list of comma separated image extensions. Only images with those extensions will be smushed.

Example usage:
java -jar smushit.jar -imageDir=/foo -verbose=true dryRun=false -imgExtensions=gif,png,jpeg

The above will smush all the images with extension gif,png,jpeg in directory foo and it's sub directories and will download the smushed images and replace original with the smushed images.

Important:
----------
Sometimes smushit converts gifs to pngs if the resulting png files are smaller. Take a look at this faq - http://developer.yahoo.com/yslow/smushit/faq.html#faq_giftopng . If this happens with your images, your image folder will have the original image as well as the newly converted png image.



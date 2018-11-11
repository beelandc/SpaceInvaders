using System;
using System.Diagnostics;

namespace SpaceInvaders
{
    class ImageRef : SLink
    {
        private Image pImage;
        public ImageRef(Image image)
                : base()
        {
            this.pImage = image;
        }

        ~ImageRef()
        {
            this.pImage = null;
        }

        public Image GetImage()
        {
            return pImage;
        }

        public void SetImage(Image newImage)
        {
            this.pImage = newImage;
        }
    }

}

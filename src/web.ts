import { WebPlugin } from '@capacitor/core';

import type { WallpaperHelperPlugin } from './definitions';

export class WallpaperHelperWeb
  extends WebPlugin
  implements WallpaperHelperPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

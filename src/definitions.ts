export interface WallpaperHelperPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

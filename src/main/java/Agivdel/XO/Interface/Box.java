package Agivdel.XO.Interface;

public enum Box {
    ZERO,//экземпляр класса Box
    X,
    O;

    //здесь указываем класс Object, а не Image, чтобы избавиться от привязки к библиотеке awt
    // (вдруг нужно будет использовать не Image, а другой класс?)
    public Object image;//поле экземпляра (т.е. у каждого объекта класса-Enum в поле image хранится его изображение)
}

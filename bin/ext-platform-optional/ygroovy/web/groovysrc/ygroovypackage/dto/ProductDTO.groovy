package ygroovypackage.dto


class ProductDTO
{
    private String catalog
    private String version
    private List<String> categories = Collections.emptyList()
    private String code
    private String name
    private String ean

    void setCatalog(String catalog)
    {
        this.catalog = catalog
    }

    String getCatalog()
    {
        catalog
    }

    void setVersion(String version)
    {
        this.version = version
    }

    String getVersion()
    {
        version
    }

    void setCategories(List<String> categories)
    {
        this.categories = categories
    }

    List<String> getCategories()
    {
        categories
    }

    void setCode(String code)
    {
        this.code = code
    }

    String getCode()
    {
        code
    }

    void setName(String name)
    {
        this.name = name
    }

    String getName()
    {
        name
    }

    void setEan(String ean)
    {
        this.ean = ean
    }

    String getEan()
    {
        ean
    }

}
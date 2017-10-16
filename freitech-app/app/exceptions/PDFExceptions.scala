package exceptions

/** PDF周りで発生するException まぁ別にとる必要ない気がするけど*/
trait PDFExceptions extends RuntimeException
class PageOverException extends PDFExceptions

